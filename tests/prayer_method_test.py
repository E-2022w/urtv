#!/usr/bin/env python3
"""The prayer calculation's choice: the app's PrayerMethod (the settings row, the city search, #pm=) and the non-Saudi cities of Cities.java (the Gulf, Egypt, the Levant, North Africa) on a plain JVM,
and the three places that turn a zone into a method (the app's labels, the launcher's PrayerMath.methodFor, the page's CONFIG.prayerMethod) kept
equal. Needs javac; without it the JVM part is skipped.   Run: python3 tests/prayer_method_test.py"""
import re
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
failed = 0


def check(name, ok, detail=""):
    global failed
    print(("PASS " if ok else "FAIL ") + name + ("" if ok else "  -> " + detail))
    failed += not ok


EXPECTED_METHODS = 11   # Ja'fari, Umm al-Qura, the four Gulf ones, Egypt, Jordan, Morocco, Algeria, Tunisia
EXPECTED_ZONES = 16   # the zones that have a method of their own: the Gulf's five, Cairo, Amman, Damascus, Beirut, Gaza, Hebron, Jerusalem (hand-entered), Casablanca, El_Aaiun (hand-entered), Algiers, Tunis
# the page: CONFIG.prayerMethod = pm=jafari ? 0 : ({'Asia/Kuwait':9, ...})[zone] || 4
tpl = (ROOT / "src/template.html").read_text(encoding="utf-8")
m = re.search(r"CONFIG\.prayerMethod = _Q\.get\('pm'\) === 'jafari' \? 0\s*:\s*\(\{([^}]*)\}\)\[CONFIG\.timezone\] \|\| 4;", tpl)
check("page: CONFIG.prayerMethod is worked out from #pm= and the zone", bool(m))
page = {z: int(i) for z, i in re.findall(r"'([A-Za-z_/]+)':\s*(\d+)", m.group(1))} if m else {}

# the launcher: PrayerMath.methodFor
pm_src = (ROOT / "android/launcher/src/main/java/sa/urtv/home/tilawah/PrayerMath.java").read_text(encoding="utf-8")
body = pm_src[pm_src.index("static int methodFor"):]
body = body[:body.index("\n    }\n")]
launcher = {}
ids = {n: int(v) for n, v in re.findall(r"(\w+) = (\d+)", re.search(r"static final int JAFARI = [^;]*;", pm_src).group(0))}   # the launcher's names for the ids
for labels, const in re.findall(r"((?:case \"[A-Za-z_/]+\": )+)return ([A-Z_]+);", body):
    for z in re.findall(r'"([A-Za-z_/]+)"', labels):
        launcher[z] = ids[const]
check("launcher and page give every zone the same method", launcher == page and len(page) == EXPECTED_ZONES, f"{launcher} vs {page}")

# the app: the label each zone gets
app_src = (ROOT / "android/app/src/main/java/sa/urtv/screensaver/PrayerMethod.java").read_text(encoding="utf-8")
labels = {}
for zs, label in re.findall(r"((?:case \"[A-Za-z_/]+\": )+)return \"([^\"]+)\";", app_src):
    for z in re.findall(r'"([A-Za-z_/]+)"', zs):
        labels[z] = label
by_id = {18: "وزارة الشؤون الدينية التونسية", 19: "وزارة الشؤون الدينية الجزائرية", 21: "وزارة الأوقاف المغربية", 9: "الكويت", 10: "قطر", 16: "الإمارات", 8: "الخليج", 5: "الهيئة المصرية للمساحة", 23: "وزارة الأوقاف الأردنية"}
check("the app names the same zones, one label per method", {z: l for z, l in labels.items()} == {z: by_id[i] for z, i in page.items()}, f"{labels}")

# every method the page and the launcher know has the same parameters in both
js = re.findall(r"^\s+(\d+):\s*\{fajr:([\d.]+),\s*isha:([\d.]+),\s*ishaMin:(\d+),\s*maghrib:([\d.]+),\s*tune:\[([-\d,]+)\]\}", tpl, re.M)
jv = {int(i): (float(f), float(ia), int(im), float(mg), tuple(int(x) for x in t.split(","))) for i, f, ia, im, mg, t in js}
jl = {}
for mid, fa, ia, im, mg, tune in re.findall(r"new Method\((\w+), ([\d.]+), ([\d.]+), (\d+), ([\d.]+)((?:, -?\d+)*)\)", pm_src):
    jl[ids[mid]] = (float(fa), float(ia), int(im), float(mg), tuple(int(x) for x in tune.replace(" ", "").split(",") if x) or (0, 0, 0, 0))
check("page and launcher have the same methods with the same angles, delays and margins", len(jv) == EXPECTED_METHODS and jv == jl, f"{jv}\n{jl}")

if shutil.which("javac"):
    src = ROOT / "android/app/src/main/java/sa/urtv/screensaver"
    with tempfile.TemporaryDirectory() as tmp:
        subprocess.run(["javac", "-encoding", "UTF-8", "-d", tmp, str(src / "PrayerMethod.java"), str(src / "Cities.java"), str(ROOT / "tests/PrayerMethodRun.java")], check=True,
                       capture_output=True)
        run = subprocess.run(["java", "-Dfile.encoding=UTF-8", "-cp", tmp, "sa.urtv.screensaver.PrayerMethodRun"], capture_output=True)
    out = run.stdout.decode("utf-8")
    print(out, end="")
    if run.returncode != 0 or "FAIL" in out:
        print(run.stderr.decode("utf-8", "replace"))
        failed += 1
else:
    print("SKIP prayer method JVM part: no javac here")
sys.exit(1 if failed else 0)

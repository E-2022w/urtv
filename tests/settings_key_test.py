#!/usr/bin/env python3
"""The settings key inside other apps (SettingsKeyLogic.java): which key events are taken, only when turned on, and which settings
screen is tried first, on tests/settings_key_cases.tsv. Needs javac; without it, skipped with a note.   Run: python3 tests/settings_key_test.py"""
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
if not shutil.which("javac"):
    print("SKIP settings key: no javac here")
    sys.exit(0)
cases = [l.split("\t") for l in (ROOT / "tests/settings_key_cases.tsv").read_text(encoding="utf-8").split("\n") if l and not l.startswith("#")]
src = ROOT / "android/launcher/src/main/java/sa/urtv/home"
with tempfile.TemporaryDirectory() as tmp:
    subprocess.run(["javac", "-encoding", "UTF-8", "-d", tmp, str(src / "SettingsKeyLogic.java"), str(ROOT / "tests/SettingsKeyRun.java")], check=True)
    out = subprocess.run(["java", "-Dfile.encoding=UTF-8", "-cp", tmp, "sa.urtv.home.SettingsKeyRun", str(ROOT / "tests/settings_key_cases.tsv")],
                         check=True, capture_output=True).stdout
got = out.decode("utf-8").split("\n")
failed = 0
for c, g in zip(cases, got):
    want, note = (c[6], c[7]) if c[0] == "decide" else (c[2], c[3])
    ok = g == want
    print(("PASS " if ok else "FAIL ") + f"settings key: {note}" + ("" if ok else f"  -> {g!r}, wanted {want!r}"))
    failed += not ok
if len(got) - 1 < len(cases):
    print("FAIL fewer answers than cases")
    failed += 1
sys.exit(1 if failed else 0)

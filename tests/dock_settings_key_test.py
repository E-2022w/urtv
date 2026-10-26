"""optimize/dock.sh settings-key: what it sends to the TV and how it refuses bad arguments (no TV needed).

    python3 tests/dock_settings_key_test.py
"""
import os, subprocess, tempfile

here = os.path.dirname(os.path.abspath(__file__))
dock = os.path.join(here, "..", "optimize", "dock.sh")
tmp = tempfile.mkdtemp()
log = os.path.join(tmp, "sent.txt")
fake = os.path.join(tmp, "fakepy")
with open(fake, "w") as f:
    f.write('#!/usr/bin/env bash\nprintf "%s\\n" "$3" >> "$SENT"\necho \'Broadcast completed: result=0, data="settings-key: off"\'\n')
os.chmod(fake, 0o755)

failed = 0
def run(*args):
    open(log, "w").close()
    r = subprocess.run(["bash", dock, *args], capture_output=True, text=True, env={**os.environ, "PY": fake, "SENT": log})
    return r.returncode, r.stdout + r.stderr, open(log, encoding="utf-8").read().strip().split("\n")

def check(name, ok, detail=""):
    global failed
    print(("PASS " if ok else "FAIL ") + name + ("" if ok else "  -> " + str(detail)))
    failed += not ok

A = "am broadcast -n sa.urtv.home/.DockReceiver -a sa.urtv.home.SETTINGS_KEY"
rc, out, sent = run("settings-key")
check("settings-key: no argument asks how it stands", rc == 0 and sent == [A] and out.strip() == "settings-key: off", (rc, out, sent))
for m in ("on", "off", "test"):
    rc, out, sent = run("settings-key", m)
    check(f"settings-key {m}", rc == 0 and sent == [A + " --es mode " + m], sent)
for bad in ("yes", "ON", "on; reboot", ""):
    rc, out, sent = run("settings-key", bad)
    check(f"settings-key: refuses {bad!r}, sends nothing", rc != 0 and sent == [""], (rc, out, sent))
print("\n" + (f"{failed} failed" if failed else "dock settings-key: all passed"))
raise SystemExit(1 if failed else 0)

"""optimize/tweaks.sh records what the TV had before it changes anything, reads every value back, and restore puts exactly
that back (a value that was not set is deleted). A fake TV with settings that persist in files; no TV needed.

    python3 tests/tweaks_journal_test.py
"""
import os, shutil, subprocess, sys, tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
STUB = r'''#!/usr/bin/env bash
# fake: python -I optimize/tv.py <args>; the TV's settings are files in $T/tv (a missing file: not set)
shift 2
S="$T/tv"; mkdir -p "$S"
case "$1" in
  --batch) while read -r l; do echo "$l" >> "$T/batch"; case "$l" in "settings put global "*|"settings delete global "*|"device_config put "*|"device_config delete "*)
             case " $REFUSE " in *" $(echo "$l" | awk '{print ($1=="settings") ? $4 : $4}') "*) ;; *) set -- $l
               case "$1 $2" in "settings put") echo "$5" > "$S/$4" ;; "settings delete") rm -f "$S/$4" ;; "device_config put") echo "$5" > "$S/$4" ;; "device_config delete") rm -f "$S/$4" ;; esac ;; esac ;; esac; done ;;
  "settings get global "*) f="$S/${1##* }"; [ -f "$f" ] && cat "$f" || echo null ;;
  "device_config get activity_manager "*) f="$S/${1##* }"; [ -f "$f" ] && cat "$f" || echo null ;;
  "df -k /data"*) echo "/dev/x 16777216 4194304 12582912 25% /data" ;;
esac
exit 0
'''
failed = 0
def check(name, ok, detail=""):
    global failed
    print(("PASS " if ok else "FAIL ") + name + ("" if ok else f"  -> {detail}"))
    failed += not ok

class Tv:
    def __init__(self, **settings):
        self.t = Path(tempfile.mkdtemp()); self.tree = self.t / "tree"; (self.tree / "optimize").mkdir(parents=True)
        shutil.copy(ROOT / "optimize" / "tweaks.sh", self.tree / "optimize")
        (self.t / "py").write_text(STUB); (self.t / "py").chmod(0o755)
        (self.t / "tv").mkdir()
        for k, v in settings.items():
            (self.t / "tv" / k).write_text(v + "\n")
    def run(self, mode, refuse=""):
        env = dict(os.environ, T=str(self.t), PY=str(self.t / "py"), REFUSE=refuse)
        r = subprocess.run(["bash", str(self.tree / "optimize" / "tweaks.sh"), mode], env=env, capture_output=True, text=True, cwd=self.tree)
        return r.returncode, r.stdout + r.stderr
    def get(self, k):
        f = self.t / "tv" / k
        return f.read_text().strip() if f.exists() else "null"
    def journal(self):
        f = self.tree / "optimize" / "run" / "tweaks-journal.txt"
        return f.read_text() if f.exists() else None
    def reboot(self):   # device_config does not survive a restart on some sets (seen on a TCL Android 14)
        (self.t / "tv" / "max_cached_processes").unlink(missing_ok=True)

# 1. nothing was set on the TV (freezer and process limit): restore deletes them, it does not write a default
tv = Tv(window_animation_scale="1.0", transition_animation_scale="1.0", animator_duration_scale="1.0")
rc, out = tv.run("apply")
check("apply: the original values are recorded first (an unset one as null)", rc == 0 and "cached_apps_freezer=null" in tv.journal()
      and "window_animation_scale=1.0" in tv.journal() and "max_cached_processes=null" in tv.journal(), f"{out} {tv.journal()}")
check("apply: the TV has the new values and nothing is flagged", tv.get("window_animation_scale") == "0.5" and tv.get("cached_apps_freezer") == "enabled"
      and "!!" not in out, out)
rc, out = tv.run("restore")
check("restore of an unset original: deleted, not written as device_default", rc == 0 and tv.get("cached_apps_freezer") == "null"
      and tv.get("max_cached_processes") == "null" and tv.get("window_animation_scale") == "1.0" and "!!" not in out, f"{out} {tv.get('cached_apps_freezer')}")
check("restore: the journal is used up (a later apply records afresh)", tv.journal() is None)

# 2. the TV had its own values: exactly those come back
tv = Tv(window_animation_scale="0.8", transition_animation_scale="0.8", animator_duration_scale="0", cached_apps_freezer="disabled", max_cached_processes="64")
tv.run("apply")
check("a limit the TV had is replaced by apply (Android's own) and recorded", tv.get("max_cached_processes") == "null" and "max_cached_processes=64" in tv.journal(), tv.journal())
tv.run("restore")
check("restore after apply: its own scales, freezer and limit, exactly",
      (tv.get("window_animation_scale"), tv.get("transition_animation_scale"), tv.get("animator_duration_scale"), tv.get("cached_apps_freezer"), tv.get("max_cached_processes"))
      == ("0.8", "0.8", "0", "disabled", "64"), str([tv.get(k) for k in ("window_animation_scale", "animator_duration_scale", "cached_apps_freezer", "max_cached_processes")]))

# 3. a second apply (run.sh again) keeps the first record
tv = Tv(window_animation_scale="1.0", transition_animation_scale="1.0", animator_duration_scale="1.0")
tv.run("apply"); first = tv.journal(); tv.run("apply")
check("a second apply keeps the first record (the TV is not 'as it came' any more)", tv.journal() == first and "window_animation_scale=1.0" in first, tv.journal())

# 4. a TV that refuses a value
tv = Tv(window_animation_scale="1.0", transition_animation_scale="1.0", animator_duration_scale="1.0")
rc, out = tv.run("apply", refuse="cached_apps_freezer")
check("apply: a value the TV does not take gets a !! line (and the setup goes on: exit 0)", rc == 0
      and "!! cached_apps_freezer: this TV did not take the value (asked for enabled, it says null)" in out and "!! window_animation_scale" not in out, out)
rc, out = tv.run("restore", refuse="window_animation_scale")
check("restore: a value the TV does not take is flagged and the journal is kept for another try", "!! window_animation_scale" in out and tv.journal() is not None, out)

# 5. after a restart of the TV
tv = Tv(window_animation_scale="1.0", transition_animation_scale="1.0", animator_duration_scale="1.0", max_cached_processes="64")
tv.run("apply"); tv.reboot()
rc, out = tv.run("restore")
check("restore after a restart (the device_config value is gone): the recorded value is put back", rc == 0 and tv.get("max_cached_processes") == "64" and "!!" not in out, f"{out} {tv.get('max_cached_processes')}")

# 6. no journal (a TV set up by an older version): Android's defaults, said
tv = Tv()
rc, out = tv.run("restore")
check("restore with no journal: Android's defaults, and it says so", rc == 0 and "No journal" in out and tv.get("window_animation_scale") == "1.0", out)

# 7. status: what it was and what it is
tv = Tv(window_animation_scale="1.0", transition_animation_scale="1.0", animator_duration_scale="1.0")
tv.run("apply"); rc, out = tv.run("status")
check("status: was and now, beside each other", "- tweak window_animation_scale: was 1.0, now 0.5" in out and "- tweak cached_apps_freezer: was null, now enabled" in out, out)
sys.exit(1 if failed else 0)

step: 1
from: UrTV supervisor
what: tonight's combined TV test, part 1 of 3 (install + Wave 0 checks). Follow tester/RULES.md for REPORT.md (no city, teams, stocks, times, network names).
build: next must be at df0e30b or newer: `git -C /c/UrTV fetch origin && git -C /c/UrTV checkout next && git -C /c/UrTV pull && git -C /c/UrTV log --oneline -1`

The build is ready: apk-next = 3.141 from df0e30b (the apk-next release has no version.txt; do not wait for it).

Before anything: write down (log only, NOT in REPORT.md) `optimize/saver.sh show` and `optimize/music.sh adhan`, so step 3 can put everything back.

| # | Do | Shots (local only) | Right when |
|---|---|---|---|
| 1 | `APK_TAG=apk-next optimize/install.sh all`, then `optimize/check-home.sh` | home | installs with no stop; Home is ours (or the stock home is back with a clear line) |
| 2 | HOME; hold RIGHT: `optimize/venv/bin/python -I optimize/tv.py input keyevent --longpress KEYCODE_DPAD_RIGHT` three times, then LEFT the same | hold-right, hold-left | the highlight moves the way of the key, one item per step, nothing freezes |
| 3 | HOME; UP to the round buttons, RIGHT to «شاشة التوقف»; OK; wait 10 s | pill-saver | OK starts the screensaver |
| 4 | `optimize/saver.sh markets off`, the screensaver (`optimize/start-saver.sh --preview`, wait 70 s); then `markets on` | markets-off, markets-on | off: no markets line; on: it is back |
| 5 | `optimize/saver.sh clocksize medium`, the screensaver; `clocksize small`; then back to the value written down | clock-medium, clock-small | the clock gets smaller; nothing overlaps or is cut |
| 6 | `optimize/saver.sh prayermethod jafari`, the screensaver; then `prayermethod auto` | jafari | with jafari Maghrib is a few minutes later; auto brings the old times back (compare the two shots yourself; do not write the times in REPORT.md) |
| 7 | `optimize/music.sh adhan short on`, `optimize/music.sh adhan` (shows adhan_short=1), then `short off` | — | turns on and off |
| 8 | «تلاوة وأذان», a reader, a surah; shot while loading; `optimize/music.sh status`; `optimize/music.sh stop` | quran-loading | «جارٍ التحميل…» while loading; status has rebuffers= and errors= |
| 9 | `optimize/share.sh --preview --problem --agent "tester" "fast test 4: preview"` (do NOT send), read the preview | — | starts «UrTV problem report»; no Wi-Fi name, IP, serial, e-mail or token in it; sends nothing |

REPORT.md: "step 1", the commit from the build line, then the table: # · OK/problem · shot names · one line (for a problem: the exact `!!` line or error, nothing personal). Push. Keep every setting as the steps leave it; step 3 restores all.

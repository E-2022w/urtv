step: 4
from: UrTV supervisor
what: the last part: Wave 2, the AlAdhan check, and putting everything back. Same build as step 3. Follow tester/RULES.md, except: AlAdhan reference times for the listed foreign cities (public data, not the owner's city) MAY go in REPORT.md.

| # | Do | Shots (local) | Right when |
|---|---|---|---|
| 1 | `optimize/saver.sh city القاهرة` (choose Cairo from the list it offers), the screensaver | cairo | under the clock «بتوقيت القاهرة · حساب الهيئة المصرية»; write the six times shown in REPORT.md as "ours" |
| 2 | `optimize/saver.sh hijrioffset 1`, the screensaver; then `hijrioffset 0` | hijri+1 | the Hijri date is one day later; the prayer times did not change |
| 3 | `optimize/saver.sh clock24 on`, the screensaver and HOME; then `clock24 off` | clock24-saver, clock24-home | 24-hour everywhere, no ص/م, nothing cut |
| 4 | `optimize/saver.sh export > optimize/run/settings-test.json`, then `optimize/saver.sh import optimize/run/settings-test.json` (preview only, NO --yes) | — | export writes JSON; the preview says nothing would change; nothing is sent |
| 5 | `optimize/tweaks.sh status` (log only) | — | each tweak shows was/now; write only "unknown" lines (if any) in REPORT.md |
| 6 | AlAdhan check (PC only): run the block below, put its output in REPORT.md | — | — |
| 7 | Put back EVERYTHING from the values written down in step 1: city (`optimize/saver.sh city <it>`), card, home-card, markets, clocksize, prayermethod, adhan settings (voice, fajr voice, short, stopkey as it was = on), hijrioffset 0, clock24 off; `optimize/music.sh stop`; press VOLUME_UP once (step 3 row 4 left the TV one step lower); remove the test app from the PIN lock list if `optimize/dock.sh pin status` still lists it; delete optimize/run/settings-test.json; then `optimize/saver.sh show` and `optimize/music.sh adhan` | — | they match step 1's lines (compare yourself; write only "restored: yes/no") |

AlAdhan block (Git Bash in C:\UrTV):
```
mkdir -p optimize/run/aladhan
curl -s https://api.aladhan.com/v1/methods > optimize/run/aladhan/methods.json
for c in "30.0444 31.2357 5 cairo" "31.9539 35.9106 23 amman" "33.5731 -7.5898 21 casablanca" "36.7538 3.0588 19 algiers" "36.8065 10.1815 18 tunis" "33.5138 36.2765 5 damascus" "33.8938 35.5018 5 beirut" "31.5326 35.0998 5 hebron"; do
  set -- $c; curl -s "https://api.aladhan.com/v1/timings/30-10-2026?latitude=$1&longitude=$2&method=$3" > optimize/run/aladhan/$4.json; done
python -c "import json;m=json.load(open('optimize/run/aladhan/methods.json'))['data'];[print(k,v.get('id'),v.get('params'),v.get('offset')) for k,v in m.items() if v.get('id') in (5,18,19,21,23)]"
python -c "import json,glob;[print(f.split('/')[-1][:-5], {k:v for k,v in json.load(open(f))['data']['timings'].items() if k in ('Fajr','Sunrise','Dhuhr','Asr','Maghrib','Isha')}) for f in sorted(glob.glob('optimize/run/aladhan/*.json')) if 'methods' not in f]"
```
REPORT.md: "step 4", the table, the Cairo "ours" times, the AlAdhan output. Push. Then tell the owner in Arabic that the test is finished and how many problems there were.

step: 3
from: UrTV supervisor
what: the install fix, then Wave 1 on the TV. Follow tester/RULES.md.

First: `git -C /c/UrTV fetch origin && git -C /c/UrTV checkout next && git -C /c/UrTV pull && git -C /c/UrTV log --oneline -1` must be cdb195c or newer. No new build is coming: tonight's fix is only in the computer-side tools (optimize/), so the apps stay at 3.141, which is correct. Do NOT wait for 3.142; `git pull` on next is all you need, then go on with row 0.

| # | Do | Shots (local) | Right when |
|---|---|---|---|
| 0 | `APK_TAG=apk-next optimize/install.sh all`, then `optimize/check-home.sh` | home | both apps install; Home is already ours so it prints «Home is already the new launcher: nothing to switch, so no safety switch is started.» and NO `!!` line; check-home OK |
| 1 | `cat optimize/run/home-switch.json` (in the log only) | — | the file exists and its state is not "switching" (write only the state word in REPORT.md) |
| 2 | `optimize/music.sh adhan` | — | `adhan_stopkey=1` |
| 3 | HOME, note which tile has the focus, `optimize/music.sh adhan now`, wait 5 s, shot; DPAD_RIGHT once; wait 2 s; shot; `optimize/music.sh adhan` | adhan-bar-home, adhan-after-key | the bar says «… · أي زر يوقف الأذان»; after the key the adhan is silent, `adhan_live=0`, the focus did NOT move, no app opened |
| 4 | `optimize/music.sh adhan now`; VOLUME_DOWN once; wait 3 s; `optimize/music.sh adhan`; `optimize/music.sh adhan stop` | — | after VOLUME_DOWN it still plays (`adhan_live=1`); `adhan stop` ends it |
| 5 | `optimize/start-saver.sh --preview`, wait 20 s, `optimize/music.sh adhan now`, wait 5 s, shot; OK; wait 3 s; shot; `optimize/music.sh adhan` | adhan-saver, adhan-saver-after | the bar shows on the screensaver; after OK the adhan stops and the screensaver stays up |
| 6 | HOME; `optimize/music.sh surah sudais 26`; wait 10 s; shot; `optimize/music.sh sleep 30`; shot; `optimize/music.sh stop` | now-pill, now-pill-sleep | the «يُتلى الآن» card is taller than the pills, two lines, nothing cut or overlapping, the pills did not move |
| 7 | Long-press OK on that card (or open «تلاوة وأذان» → a surah); hold RIGHT 3 s (`--longpress` ×3); `optimize/music.sh status` | player-hint | hint «يمين/يسار: ١٠ ثوانٍ»; position moved forward about 30–40 s per second held, never backwards |
| 8 | `optimize/saver.sh home-card nextprayer`, HOME, shot; then `optimize/saver.sh card nextprayer` | home-nextprayer | the home card shows the next prayer and «بعد …» in the same words as the screensaver panel; `card nextprayer` is refused with a `!!` line |
| 9 | `optimize/saver.sh city المنامة`, the screensaver, shot; HOME, shot | city-other-saver, city-other-home | under the clock «بتوقيت المنامة · حساب الخليج»; no first-run note anywhere |
| 10 | If teams are followed: `optimize/saver.sh card matches`, the screensaver, shot (else skip) | matches | crests beside names; a row with long names shows full names without crests |
| 11 | Parental (if `optimize/dock.sh pin status` exists): `optimize/dock.sh pin set 4321`; in the launcher settings «الرقابة الأبوية» lock one app; open it from HOME: shot of the keypad; type 1111 then 4321 with the number keys; then `optimize/dock.sh pin off` | pin-pad | keypad shows; 1111 refused; 4321 opens the app; after `pin off` nothing asks |
REPORT.md: "step 3" + the table (no city/teams/times; for Manama write only "city line OK/problem"). Push. Leave settings as they are; step 4 restores.

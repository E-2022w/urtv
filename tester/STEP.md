step: 5
from: UrTV supervisor
what: today's build (27 Oct) on apk-next: install, home, keys, screens, the new settings. Follow tester/RULES.md (REPORT.md: no city, teams, stocks, times, Wi-Fi, IPs, serials, input names). The owner helps with the remote where a row says "owner"; ask in Arabic.

Before anything:
- `git fetch origin && git checkout next && git pull && git log --oneline -1` must show a1d2063 or newer.
- Wait until the release apk-next has version.txt with 3.143 or newer: `curl -sL https://github.com/tshi-dev/UrTV/releases/download/apk-next/version.txt`. If it's not there after 20 minutes, write "no build" in REPORT.md and stop.
- Write down LOCALLY ONLY: `optimize/saver.sh show`, `optimize/dock.sh show`, `optimize/dock.sh input-names`, `optimize/music.sh adhan`, `optimize/dock.sh pin status`, `optimize/dock.sh familydns status`.

| # | Do | Shots (local) | Right when |
|---|---|---|---|
| 1 | `APK_TAG=apk-next optimize/install.sh all`, then `optimize/check-home.sh` | home | installs; Home already ours / nothing to switch; no `!!` |
| 2 | `optimize/saver.sh show` (first line only into REPORT.md) | — | version 3.143 or newer |
| 3 | Android 14 check: `optimize/start-saver.sh --preview`, wait 20 s; WHILE it shows run `optimize/saver.sh city جدة`, wait 30 s; then let the real screensaver start (`optimize/start-saver.sh`), wait 20 s; `adb logcat -d -s AndroidRuntime > optimize/run/crash.log` | city-live, dream | no crash (crash.log has no FATAL for sa.urtv); the line under the clock changed to Jeddah while showing |
| 4 | Screensaver settings on the TV: move to a row in the middle (e.g. «حجم الساعة»), OK to change it | focus-row | the focus stays on that row |
| 5 | `optimize/saver.sh clocksize medium`, saver, shot; `clocksize small`, shot; `optimize/saver.sh card2 weather`, shot; `clocksize large`, shot; `card2 none` | clock-medium, clock-small, clock-small-card2, clock-large | each looks designed, nothing cut; the second card only with small; large looks as before |
| 6 | `optimize/saver.sh card adhkar` (or the card holding adhkar), saver | adhkar | a dhikr, not a hadith |
| 7 | `optimize/saver.sh wxmore on`; restart the saver 3 times; shot; `wxmore off` | wxmore | humidity and wind show ONCE |
| 8 | `optimize/saver.sh sunrise on`, `prayerbar on`, `months levant`, `markets tasi,gold,silver,oil`; saver + HOME shots; put each back | w2-saver, w2-home | bar shows; كانون/تشرين months on both; Brent fits; nothing cut |
| 9 | HOME: long OK on a favourite → «تعديل المفضلة»; OK on a tile, RIGHT twice, OK; DOWN to its ×, OK; UP to «تراجع» within 5 s, OK; Back; `optimize/dock.sh show` | fav-hint, fav-lifted, fav-x, fav-undo | the tile lifts and moves (RIGHT toward the start in Arabic), × removes, «تراجع» restores, Back saves (dock.sh show matches). Put the original order back |
| 10 | `adb logcat -c`; with HOME in front press YouTube, Netflix, Settings, ★ on the remote (owner); after each: Back to HOME and press the SAME key again; then `adb logcat -d -s UrTVKeys > optimize/run/keys.log` | key-* | each opens, the 2nd press too. REPORT.md: per key "reached us yes/no" (keycodes only) |
| 11 | Open «ترتيب المفضلة» (settings), «المهام», «الرقابة الأبوية» (Back at the PIN); surah grid: go past a row's first and last tile | rtl-dock, rtl-tasks, rtl-parental | tidy, nothing on the wrong side, arrows move as they point, no jump to search |
| 12 | `optimize/saver.sh listenpill off`, HOME, shot; long OK on an app: the menu has «تلاوة وأذان…» and «إعدادات الأذان…»; `optimize/music.sh adhan now`, then press RIGHT once; `listenpill on` | no-tilawa, menu-adhan | the button is gone and focus moves across the gap; the adhan plays with its bar and a key stops it |
| 13 | Screensaver settings → row «الأذان» (owner) | adhan-row | opens the adhan tab of «تلاوة وأذان»; change nothing |
| 14 | Inputs: `optimize/dock.sh input-names` (log only); `optimize/dock.sh input-name <the first id> 'PS5'`; open «المصادر» from HOME; then set the name back to empty with `input-name <id> ''` | inputs | «PS5» shows in the list; after clearing, the TV's own name is back. Never write names in REPORT.md |
| 15 | `optimize/dock.sh pin set 4321`; `pin lock @urtv`; open the home settings: keypad; wrong PIN twice, then 4321; `pin unlock @urtv`; `pin off` | pin-pad | keypad shows, wrong refused, right opens; with the PIN off nothing asks |
| 16 | Capture real outputs for the team (LOCAL file optimize/run/real-outputs.txt; REPORT.md gets only "saved: yes"): `adb shell cmd package query-activities --brief -a android.intent.action.MAIN -c android.intent.category.HOME`, `adb shell cmd package query-services --brief -a android.service.dreams.DreamService`, `adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME`, `adb shell appops get com.tcl.tvweishi AUTO_START 2>&1` | — | file saved |
REPORT.md: "step 5", the table (OK/problem · shot names · one line), the version, the keys yes/no, any `!!` lines. Push. Tell the owner in Arabic the step is done and how many problems. Step 6 (family DNS, the update screen, phone pairing with the typed code, the real update test, restore) follows.

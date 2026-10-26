step 3

build: next at cdb195c. The release API kept printing "version 3.141" for 15 minutes (8 tries); went on with it. Installed apps: 3.141.

| # | OK/problem | shots | note |
|---|---|---|---|
| 0 | OK | home | both installed; printed «Home is already the new launcher: nothing to switch, so no safety switch is started.»; no !! line; check-home OK |
| 1 | OK | — | home-switch.json exists, state: aborted |
| 2 | OK | — | prints adhan_stopkey=on (word, not 1) |
| 3 | OK (weak) | adhan-before, adhan-bar-home, adhan-after-key | bar «الأذان · أي زر يوقف الأذان»; after RIGHT silent, adhan_live=0, home still in front, no app opened. Focus was on the right-end tile, so RIGHT could not have moved it anyway |
| 4 | OK | — | after VOLUME_DOWN adhan_live=1, playing=1; adhan stop: adhan_live=0 (TV volume is one step lower now) |
| 5 | OK | adhan-saver, adhan-saver-after | bar shows on the screensaver; OK stops the adhan and the screensaver stays |
| 6 | OK | now-pill, now-pill-sleep | card taller than the pills, two lines, sleep note added, nothing cut, pills did not move. Did row 7 before the stop |
| 7 | OK / check | card-focus2, player-hint | UP from the tiles lands on «المصادر», not the card (long OK there opened the sources menu; closed with BACK, nothing chosen); RIGHT once reaches the card («مطولاً: فتح»). Hint «يمين/يسار: ١٠ ثوانٍ» shows. 3 x --longpress RIGHT moved +62 s forward (about 20 s per press), never back; rebuffers=6 errors=0 |
| 8 | OK | home-nextprayer | same words as the screensaver panel; `card nextprayer` refused: «!! nextprayer is a home-screen card only (the screensaver's bottom-right panel already shows the next prayer): optimize/saver.sh home-card nextprayer» |
| 9 | OK | city-other-saver, city-other-home | city line OK on both; no first-run note |
| 10 | OK | matches | crests beside names; only one match listed, so no long-name row to check |
| 11 | OK | pin-pad, pin-wrong, pin-right | the lock is set from long OK on an app → «الرقابة الأبوية…» (the gear pill opens Android settings). Locked one app; keypad shows; 1111 refused («بقي ٤ محاولات»); 4321 opens it; after pin off it opens with nothing asking. pin status still lists that app as chosen |

Left as the steps leave them (step 4 restores): other city, home card nextprayer, screensaver card matches, PIN off with one app still on the lock list, TV volume one step lower.

step: 6
from: UrTV supervisor
what: the REAL update from the phone (3.144 → 3.145+), the new settings menu, family DNS, phone pairing by typed code, and putting everything back. Follow tester/RULES.md. The owner does the phone and remote parts; ask in Arabic.

Before anything:
- Finish step 5 rows 10 and 13 if they're still open, and push the step 5 report.
- Run `optimize/saver.sh updatechannel next`, then `optimize/saver.sh show | grep updatechannel`. It must say next.
- Write down the installed version from `optimize/saver.sh show`, first line.

| # | Do | Shots (local) | Right when |
|---|---|---|---|
| 1 | Wait until `curl -sL https://github.com/tshi-dev/UrTV/releases/download/apk-next/version.txt` is ABOVE the installed version (the supervisor pushes it; up to 30 min) | — | a newer version is listed |
| 2 | Owner: on the TV, Settings → «الجوال والتحديث» (or wherever phone control is) → تحكم من الجوال → تشغيل. Scan the QR code with the phone, then press سماح on the TV. Then ALSO try pairing a second time by TYPING the 6-digit code shown on the TV (Arabic TV language) | phone-qr, phone-allow | the phone connects; the typed code is accepted (it was always refused before) |
| 3 | Owner, on the phone page: «تحديث تلفزيونك» → افحص → ثبّت. The TV shows Android's install screen(s); the owner confirms with the remote (screensaver app first, then the home app) | phone-update, install-1, install-2 | both install; no «ما فيه اتصال» while the internet works |
| 4 | `optimize/saver.sh show` (first line), and `adb shell dumpsys package sa.urtv.home \| grep versionName` | — | the new version on both apps |
| 5 | Open the settings app: the new main menu. Shots of the menu, then open «الموقع والصلاة» and press Back | settings-hub, settings-sub | the tiles show with their summaries; Back returns to the same tile |
| 6 | Favourites edit, again: remove one app with ×, then press UP → «تراجع» → OK; then try Back within 5 s on another removal; put the order back | fav-undo2 | UP reaches «تراجع»; Back undoes |
| 7 | `optimize/saver.sh card adhkar`, the screensaver | adhkar2 | a dhikr, NOT the hadith |
| 8 | `optimize/dock.sh familydns status`; `familydns on`; `familydns status`; open a website in the TV browser; `familydns off`; `familydns status` | — | on: says on after its checks; off: back to the start value. Any `!!` line goes in REPORT.md |
| 9 | `optimize/saver.sh updatechannel latest`, then `saver.sh show \| grep updatechannel` | — | latest |
| 10 | Put back EVERYTHING from step 5's start lines (city back from Jeddah, cards, favourites order, input names, pin, familydns); `optimize/music.sh stop`; compare `saver.sh show` and `dock.sh show` | — | "restored: yes/no" |

REPORT.md: "step 6", the table, the versions (before/after), and any `!!` lines. Push. Tell the owner in Arabic that the test is finished and how many problems there were.

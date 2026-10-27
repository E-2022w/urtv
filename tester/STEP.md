step: 6
from: UrTV supervisor
what: the REAL update from the phone (3.144 → the newest apk-next), then every new feature of this release on the real TV, then putting everything back. Follow tester/RULES.md. The owner does the phone and remote parts; ask him in Arabic, one row at a time.

OWNER TESTS TOMORROW (28 Oct). Wait until he writes «رجعت» in this chat (or a new STEP.md arrives). Until then do ONLY what needs no owner: "Before anything", row 1, and rows 7 and 8. Do NOT ask him or send notifications before «رجعت». When he writes «رجعت»: do the rows that need him FIRST, back to back, so he is free sooner: 2, 3, 5, 6, 9-17, 19 (row 4 right after row 3: it needs no one). Ask him once at the end if anything else looked wrong. Then, without him: 18, 20, 21. The update target is whatever apk-next shows at that time.

Before anything:
- Finish step 5 rows 10 and 13 if they're still open, and push the step 5 report.
- Write down these start lines (they are what row 21 puts back): `optimize/saver.sh show`, `optimize/dock.sh show`, `adb shell settings get secure enabled_accessibility_services`, `adb shell settings get global private_dns_mode`.
- Run `optimize/saver.sh updatechannel next`, then `optimize/saver.sh show | grep updatechannel`. It must say next.
- Write down the installed version (`optimize/saver.sh show`, first line).

| # | Do | Shots (local) | Right when |
|---|---|---|---|
| 1 | Wait until `curl -sL https://github.com/tshi-dev/UrTV/releases/download/apk-next/version.txt` is ABOVE the installed version | — | a newer version is listed |
| 2 | Owner: Settings → «الجوال والتحديث» → تحكم من الجوال → تشغيل. Scan the QR with the phone, press سماح on the TV. Then pair a second time by TYPING the 6-digit code shown on the TV | phone-qr, phone-allow | the phone connects; the typed code is accepted |
| 3 | Owner, on the phone page: «تحديث تلفزيونك» → افحص → ثبّت. He confirms Android's install screens with the remote (screensaver app first, then the home app) | phone-update, install-1, install-2 | both install; no «ما فيه اتصال» while the internet works |
| 4 | `optimize/saver.sh show` (first line), and `adb shell dumpsys package sa.urtv.home \| grep versionName` | — | the new version on both apps |
| 5 | Open the settings app (also try the remote's Settings key while the home is in front): the new main menu. Shots, then open «الموقع والصلاة» and press Back | settings-hub, settings-sub | the key opens it; the tiles show their summaries; Back returns to the same tile |
| 6 | Favourites edit: remove one app with ×, press UP → «تراجع» → OK; then Back within 5 s on another removal; put the order back | fav-undo2 | UP reaches «تراجع»; Back undoes |
| 7 | `optimize/saver.sh card adhkar`, the screensaver | adhkar2 | a dhikr, NOT the hadith |
| 8 | `optimize/dock.sh familydns status`; `familydns on`; `status`; open a website in the TV browser; `familydns off`; `status` | — | on after its checks; off goes back to the start value; any `!!` line goes in REPORT.md |
| 9 | Phone page, all 4 screens: shots of each. On the TV, check they look the same dark style; turn the phone's light mode on once, check every screen turns light, turn it back | phone-remote, phone-apps, phone-now, phone-settings, phone-light | one style on all 4; the toggle changes all of them |
| 10 | Typing: `optimize/dock.sh type-setup`. Owner opens YouTube's search, types «قرآن» on the phone's text field, presses «بحث» | type-yt | the text appears in YouTube's search box and the search runs |
| 11 | Home: the round «مركز التحكم» pill (between «المهام» and «تلاوة وأذان») → shot of the panel. Try «الشبكة», «اختبار السرعة», «حرّر الذاكرة», «مخرج الصوت». Back closes it. Do NOT press «أطفئ الآن» until last; then press it, and the owner wakes the TV with the remote | center, center-speed, center-mem | each tile shows a sane value; Back returns focus to the pill; «أطفئ الآن» turns the screen off (or says how to turn the service on) |
| 12 | «المصادر»: the HDMI ports. If the PS5 or another CEC device is on, then in standby | inputs-cec, inputs-standby | the device's own name shows; the state changes to standby; the owner's own renames still win |
| 13 | Settings → الشاشة الرئيسية → المظهر: dark → light → auto → back to dark. The home follows within a minute | look-light, look-dark | light is readable; dark is the default and returns exactly |
| 14 | Settings → الشاشة الرئيسية → صفوف القنوات: allow the TV listings once («السماح»), show «أكمل المشاهدة» plus one app row, OK on one card | rows-setup, rows-home, rows-open | the rows show under the favourites; OK opens the card in its own app; with nothing allowed, the home is unchanged |
| 15 | Move fast along the favourites and rows, and hold the arrow key | — (owner's word) | the focus grows smoothly; held keys don't lag or jump |
| 16 | «كل التطبيقات»: shot | allapps2 | the tiles line up with the favourites row |
| 17 | Settings → الشاشة الرئيسية: «زر تلاوة وأذان» hidden, then the «الأذان» row opens the adhan tab; show the pill again | hide-pill, adhan-row | the pill goes; the adhan row works; the pill comes back |
| 18 | The bell: just look at the top row | bell | no bell unless the Play Store has updates; nothing broken |
| 19 | LOOK ONLY (do not press «ابدأ»): open «إعداد تلفزيوني» from the apps row (the TV-only setup). Shot of its first screen, then Back | tvsetup | it opens and Back leaves without changing anything |
| 20 | `optimize/saver.sh updatechannel latest`, then `saver.sh show \| grep updatechannel` | — | latest |
| 21 | Put back EVERYTHING from the start lines: city, cards, favourites order, input names, pin, familydns, appearance (dark), channel rows (as at start), the pill, the accessibility services (`adb shell settings put secure enabled_accessibility_services '<start value>'`), phone control off; `optimize/music.sh stop`. Compare `saver.sh show` and `dock.sh show` with the start lines | — | "restored: yes/no" |

REPORT.md: "step 6", the table with OK / problem per row, the versions (before/after), and any `!!` lines. No personal data (RULES.md). Push. Tell the owner in Arabic that the test is finished and how many problems there were.

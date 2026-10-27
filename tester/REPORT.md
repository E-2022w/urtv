step 6

Versions: before 3.144, after 3.145 (both apps, updated from the phone page). updatechannel: latest → next for the test → latest again.

| # | OK/problem | shots | note |
|---|---|---|---|
| 1 | OK | — | apk-next showed 3.145 |
| 2 | OK | phone-qr | QR scan and the typed 6-digit code both accepted (owner). Owner: the pairing screen should fit on one page with no scrolling, and the address and code should stand out more (the code sits at the very bottom edge) |
| 3 | OK / check | install-1 | owner saw ONE Android install screen (screensaver app) and pressed Update; both apps still went to 3.145. No «ما فيه اتصال» |
| 4 | OK | — | version: 3.145; sa.urtv.home versionName=3.145 |
| 5 | OK / problem | settings-key, settings-hub, settings-sub | remote Settings key opens Android's own settings (com.android.tv.settings/.MainSettings), not ours. Our menu: 5 tiles with summaries; «الموقع والصلاة» opens; Back returns to the same tile. Problem: after going down and back up, the screen does not scroll to the top, so the title/header stays hidden (same in «ترتيب المفضلة», shot header-missing, and the main menu, owner). One shot showed two settings pages drawn over each other (settings-overlap, maybe mid-animation) |
| 6 | OK | — | owner: UP reaches «تراجع», Back undoes. Owner keeps the new favourites order |
| 7 | OK / check | adhkar2, adhkar3 | on 3.144 `card adhkar` showed a verse/hadith card. On 3.145 it shows «أذكار المساء» with only when they start (no dhikr text at night) |
| 8 | problem (!!) | — | `familydns on` printed: «!! familydns: the internet did not work through family.adguard-dns.com, so the old Private DNS setting was put back»; status stayed off; off: back to start (Private DNS null) |
| 9 | OK | — | owner: one dark style on all 4 phone screens; light mode changes all of them. Owner: the bottom icons on the phone page need their own design |
| 10 | problem | — | `dock.sh type-setup` turned the service on (bound: «تلفزيونك: الكتابة من الجوال»), but typing «قرآن» from the phone did nothing in YouTube's search; no TypeService lines in logcat |
| 11 | problem | center, center-mem | panel shows 8 tiles. Second line of 3 tiles is cut at the bottom (الشبكة, حرّر الذاكرة, أطفئ الآن). «الشبكة» opens settings. «اختبار السرعة» works. «مخرج الصوت» opens sound settings with nothing about the output. «حرّر الذاكرة» says nothing was freed («الذاكرة مرتاحة») while MemAvailable was about 570-620 MB of 2.4 GB and swap nearly full. Reopening the panel puts focus on the top-right tile. «أطفئ الآن»: dimmed screensaver, no message (its tile says the service is off). Owner: add our UrTV settings as a tile here |
| 12 | problem | inputs-cec | owner pressed the source button: it opened the TV's own TCL/Google Inputs panel in English (Google TV Home, AirPlay, TV + Apps), not our list; no HDMI ports in it. CEC/standby not tested |
| 13 | OK | look-light, look-auto, look-dark | light readable; auto (night) is dark; dark returns exactly |
| 14 | problem (owner) | rows-home | rows show under the favourites, but owner: «a mess, I don't like it»: big gaps between rows, first card of each row cut at the left, row titles in English, the home background/clock gone. Owner wants ONLY «أكمل المشاهدة», no other app rows (they look like ads). Left with only «أكمل المشاهدة» shown, as the owner asked |
| 15 | not done | — | owner went to sleep before this row |
| 16 | OK | allapps2 | «كل التطبيقات» right under «أكمل المشاهدة»; its edges line up with the favourites row |
| 17 | OK | hide-pill, pill-back | pill hidden then back (via saver.sh listenpill); adhan row checked in step 5 |
| 18 | OK | bell | no bell on the top row |
| 19 | OK | tvsetup | «إعداد تلفزيوني» opens with its 5 steps and two buttons; Back left without pressing anything |
| 20 | OK | — | updatechannel: latest |
| 21 | partly | — | restored: city, cards (verse, card2 none, home cards none), appearance dark, pill on, accessibility services back to the start value, pin not set, familydns off, music stopped, input names (TV's own). Favourites kept as the owner wants. Channel rows: only «أكمل المشاهدة» (owner). NOT restored: «تحكم من الجوال» is still on: its row is not in any section of the 3.145 settings menu I could find |

Other, from the owner and checks:
- Owner: moving between apps feels slower than the last update. At that time: MemAvailable about 570 MB of 2.4 GB, swap almost used up (84 MB free of 917 MB); sa.urtv.screensaver used about 45% CPU while no screensaver was showing.
- Owner said the screensaver did not come after 45 s. Re-checked with nobody touching the TV: it started by itself after about 45 s (DreamActivity, ScreenDream). The earlier miss was most likely the agent's own commands waking the TV.
- Owner: UrTV settings sometimes came up by themselves; at least two of those were the agent opening them over ADB.

!! lines: the familydns line in row 8.

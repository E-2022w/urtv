step 4

Same build as step 3 (apps 3.141, next cdb195c).

| # | OK/problem | shots | note |
|---|---|---|---|
| 1 | problem (once) | cairo, cairo2, cairo-times | first `start-saver.sh --preview` printed "dream" and the screensaver still showed the previous city; after BACK, HOME and a new start ("preview") it showed «بتوقيت القاهرة · حساب الهيئة المصرية». The screensaver shows only the next prayer, so the six times were read with `card prayers` for a moment (card restored in row 7) |
| 2 | OK | hijri+1 | Hijri date one day later (Ramadan countdown one day less); prayer times unchanged; back to 0 |
| 3 | OK | clock24-saver, clock24-home | 24-hour on both, no ص/م, nothing cut; back to off |
| 4 | OK | — | export wrote JSON (947 bytes); import preview: «Nothing to change: the TV already has these settings.»; nothing sent; file deleted in row 7 |
| 5 | OK | — | «Tweaks: none recorded»; no "unknown" lines |
| 6 | problem (script) | — | the last python line of the block failed on Windows: UnicodeDecodeError 'charmap' (cp1252) reading the JSON; ran the same line again with PYTHONUTF8=1, output below |
| 7 | OK | — | restored: yes. The lock list kept the test app after pin off, and there is no command for one lock, so `optimize/dock.sh pin reset` was used (nothing parental was set before the test: now pin not set, locked none). VOLUME_UP pressed once |

Cairo, ours (screensaver, 26 Oct 2026, prayermethod auto): Fajr 05:37, Sunrise 07:04, Dhuhr 12:39, Asr 15:50, Maghrib 18:13, Isha 19:31
Note: the AlAdhan block asks for 30-10-2026; Egypt's clocks go back between the two dates, so AlAdhan's Cairo times are about one hour earlier than ours.

AlAdhan output:
```
EGYPT 5 {'Fajr': 19.5, 'Isha': 17.5} None
TUNISIA 18 {'Fajr': 18, 'Isha': 18} None
ALGERIA 19 {'Fajr': 18, 'Isha': 17} None
MOROCCO 21 {'Fajr': 19, 'Isha': 17} None
JORDAN 23 {'Fajr': 18, 'Maghrib': '5 min', 'Isha': 18} None
```

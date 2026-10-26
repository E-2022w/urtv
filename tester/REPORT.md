step 1

build: next at df0e30b. apk-next/version.txt printed "Not Found" for 20 minutes (10 tries); went on as STEP.md says. Installed apps: screensaver 3.141, home 3.141.

!! line from `APK_TAG=apk-next optimize/install.sh all` (twice, same both times; TV not restarted):
!! The safety switch did not start on the TV (no heartbeat), so the home was NOT switched and nothing was changed.
   Run the same command again; if it repeats, restart the TV, check it is on Wi-Fi, and try once more.

| # | OK/problem | shots | note |
|---|---|---|---|
| 1 | problem | home | both APKs installed (3.141), then the !! line above twice; check-home.sh: "OK: Home opens تلفزيونك" (Home was already ours) |
| 2 | check | hold-right, hold-left, hold-right2 | moves the way of the key, no freeze; but 3 long-presses moved 4 items each way (hold repeats); first RIGHT run started at the right end so nothing moved |
| 3 | problem (steps) | pill-up, pill-right1, pill-left1..6, pill-moon, pill-saver | UP lands on the right end (المصادر); RIGHT does nothing there; «شاشة التوقف» (moon) is 5 x LEFT. OK on it starts the screensaver |
| 4 | OK | markets-off, markets-on | off: no markets line; on: back |
| 5 | OK | clock-medium, clock-small | clock smaller each time, nothing overlaps or cut; set back to large |
| 6 | OK | jafari, jafari-auto | the next prayer on screen was Fajr (Maghrib not shown at this hour): later with jafari, auto brings the old time back |
| 7 | OK | — | prints adhan_short=on / adhan_short=off (words, not 1/0) |
| 8 | check | q-open, q-surahs, quran-loading, q-playing | plays; «جارٍ التحميل…» not seen (shot showed 0:00 already loaded); status has rebuffers=0 errors=0; stopped |
| 9 | OK | — | preview starts «UrTV problem report»; no Wi-Fi name, IP, serial, e-mail or token; not sent |

Settings left as the steps leave them: markets on, clocksize large, prayermethod auto, adhan short off.

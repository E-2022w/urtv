step: 7a
from: UrTV supervisor
what: the fixes from step 6 that need NO owner, on the new apk-next build (3.146 or newer), plus the slowdown measurement. Follow tester/RULES.md. Do NOT ask the owner anything and send him no notifications for this step: he tests the rest (with the new glass look) in step 7b later today. Drive the TV with `adb shell input keyevent …` and `adb shell screencap`.

Before anything:
- Write down the start lines: `optimize/saver.sh show`, `optimize/dock.sh show`, `adb shell settings get secure enabled_accessibility_services`, `adb shell settings get global private_dns_mode`.
- Wait until `curl -sL https://github.com/tshi-dev/UrTV/releases/download/apk-next/version.txt` is above 3.145. Download urtv-screensaver.apk and urtv-home.apk from apk-next and install both with `adb install -r` (screensaver first). Write down the version from `optimize/saver.sh show`, first line.

| # | Do | Shots (local) | Right when |
|---|---|---|---|
| 1 | SLOWDOWN, idle: leave the TV on the home screen 2 min untouched, then `adb shell dumpsys cpuinfo \| grep -E 'urtv\|webview\|sandboxed'`, `adb shell cat /proc/meminfo \| grep -E 'MemAvailable\|SwapFree'`, `adb shell dumpsys meminfo sa.urtv.screensaver \| grep -E 'TOTAL\|WebViews\|Views:'`, `adb shell dumpsys activity services sa.urtv.screensaver \| grep ServiceRecord` | — | screensaver CPU near 0 %; WebViews 0; no service but UpdateJob |
| 2 | SLOWDOWN, Home churn: 5 times: open YouTube (`adb shell monkey -p com.google.android.youtube.tv 1`), wait 5 s, press Home (`input keyevent 3`), wait 3 s. Right after: the cpuinfo line again, and `adb shell pidof sa.urtv.screensaver` before and after | — | write the numbers; compare with step 6 (45 % CPU, 570 MB available, swap almost full) |
| 3 | Settings key: on the home screen `input keyevent 83`, then `adb shell dumpsys activity activities \| grep -m1 topResumedActivity`; Back; the same with `input keyevent 176` | key83, key176 | our settings (sa.urtv.screensaver/.SettingsActivity) open, not com.android.tv.settings |
| 4 | Our settings: «الجوال والتحديث» tile. «تحكم من الجوال» is the FIRST row; open it and turn phone control OFF (it was left on after step 6) | phone-row, phone-off | the row is there and first; phone control says it is off |
| 5 | Control Center: the round pill on the home's top row, OK. Shot. Move through every tile; open «مخرج الصوت» | center2 | no tile cut at the bottom; no «حرّر الذاكرة»; a tile «إعدادات تلفزيونك» opens OUR settings; «مخرج الصوت» names the output (write only its TYPE in the report, e.g. "HDMI ARC" or "Bluetooth", never the device's name); Back, then reopen: focus is on the last tile used |
| 6 | Family DNS: `optimize/dock.sh familydns status`; `familydns on` (it may take up to 90 s); `status`; `adb shell settings get global private_dns_mode`; `adb shell logcat -d \| grep -i family-dns \| tail -20`; then `familydns off`; `status` | — | on says on; off goes back to the start value. If on fails again, paste the family-dns log lines (they hold no personal data) and every `!!` line |
| 7 | Adhkar: `optimize/saver.sh card adhkar`, start the screensaver (`adb shell am start -n com.android.systemui/.Somnambulator` or wait 45 s untouched) | adhkar4 | a dhikr TEXT shows, not only a heading |
| 8 | Restore everything from the start lines (card, familydns, accessibility services); compare `saver.sh show` and `dock.sh show` | — | "restored: yes/no" |

REPORT.md: "step 7a", the table, the before/after numbers of rows 1-2 next to step 6's, any `!!` lines. No personal data (RULES.md). Push. Do not message the owner.

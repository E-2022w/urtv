step 5

build: next at a1d2063; apk-next/version.txt printed "Not Found" for 16 minutes, then 3.144. saver.sh show: "version: 3.144".

| # | OK/problem | shots | note |
|---|---|---|---|
| 1 | OK | home | installed; «Home is already the new launcher: nothing to switch, so no safety switch is started.»; check-home OK; no !! |
| 2 | OK | — | version: 3.144 |
| 3 | OK / check | city-live, dream | city changed while the preview showed: the line under the clock changed at once; crash.log: no FATAL. `start-saver.sh` (real dream) printed «not started» on this TV, so the dream shot is the home screen |
| 4 | OK | focus-row | «حجم الساعة» → chooser → متوسطة: focus stays on the row, value updated |
| 5 | OK (step text) | clock-medium, clock-small, clock-small-card2, clock-large, clock-large-card2 | `card2 weather` is refused: «error: panel2 is one of shows prayers forecast hourly prayerrow matches verse wisdom adhkar stocks tasks year smart today none»; used `card2 forecast`: second card only with small; large looks as before; nothing cut |
| 6 | problem | adhkar | `card adhkar` saved (panel: adhkar) but the card shows «حديث اليوم» (a hadith), not a dhikr. With `card verse` the same hadith card showed |
| 7 | OK | wxmore | after 3 restarts humidity and wind show once |
| 8 | OK | w2-saver, w2-home | prayer bar shows; تشرين الأول on both screens; نفط برنت fits; nothing cut; all four put back |
| 9 | problem | fav-hint, fav-lifted, fav-moved, fav-dropped, fav-x, fav-removed, fav-undo | lift and move work (RIGHT moves toward the start), × removes, Back saves (dock.sh show matched). «تراجع» shows at the far left above the last tile, but UP from the tile row did not reach it: OK lifted the tile under focus instead and the 5 s passed. Original order put back with `dock.sh set` |
| 10 | problem | — | owner pressed YouTube, Netflix, Settings, ★ twice each. Owner: Settings did not open either time; in Netflix Back does not return to HOME (Home key needed). keys.log (UrTVKeys) |
| 11 | OK | rtl-dock, rtl-tasks, rtl-parental, rtl-grid0, rtl-grid-right, rtl-grid-6, rtl-grid-past | tidy, right-aligned; surah grid stops at both row ends, no jump. No PIN was set, so parental opened without the keypad |
| 12 | OK | no-tilawa, no-tilawa-focus, menu-adhan, adhan-bar5 | button gone, focus crosses the gap; menu has «تلاوة وأذان…» and «إعدادات الأذان…»; adhan bar shows, RIGHT stops it (playing=0); listenpill back on |
| 13 | OK | — | owner: the «الأذان» row opens the adhan tab of «تلاوة وأذان»; nothing changed |
| 14 | OK | inputs, inputs-cleared | the custom name shows in «المصادر»; after clearing the TV's own name is back |
| 15 | OK | pin-pad, pin-wrong1, pin-wrong2, pin-right, pin-off-menu | keypad («قائمة التطبيق» locked); two wrong refused (4, then 3 tries left); right opens; unlock + pin off: nothing asks; pin status back to: not set, locked none |
| 16 | OK | — | saved: yes |

Keys (row 10), reached us (UrTVKeys):
- YouTube: no (no event logged; owner: it opens)
- Netflix: no (no event logged; owner: it opens)
- Settings: yes, probably keycode 83 (logged 5 times); nothing opens
- ★: not confirmed (owner did not say)

!! lines: none.
Left as the steps leave them (step 6 restores): city changed in row 3. Everything else put back.

step: 0
from: UrTV supervisor
what: connection check only. Change nothing on the TV.

1. git -C /c/UrTV log --oneline -1 origin/next
2. optimize/venv/bin/python -I optimize/tv.py --check
3. optimize/saver.sh show
4. optimize/music.sh adhan

Write to tester/REPORT.md on work/tester-report: "step 0", then each command and its output (cut to 15 lines each; remove any Wi-Fi name, IP address or serial number if one appears), then push.

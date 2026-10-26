step: 2
from: UrTV supervisor
what: a short DIAGNOSTIC (5 minutes) for the step 1 problem «The safety switch did not start on the TV (no heartbeat)». Nothing here changes the home or any setting. Follow tester/RULES.md.
Thank you: step 1 is read. The hold and pill notes were mistakes in my steps, not problems.

Run each block in Git Bash in C:\UrTV and copy the exact output (it holds no personal data) into REPORT.md.

A. How is the background job started? Same way as install.sh does it (through --batch):
```
printf '%s\n' "nohup sh -c 'echo \$\$ > /data/local/tmp/urtv_tA.pid; i=0; while [ \$i -lt 40 ]; do date +%s > /data/local/tmp/urtv_tA.hb; sleep 3; i=\$((i+1)); done' > /dev/null 2>&1 &" | optimize/venv/bin/python -I optimize/tv.py --batch; echo rcA=$?
sleep 8
optimize/venv/bin/python -I optimize/tv.py 'p=$(cat /data/local/tmp/urtv_tA.pid 2>/dev/null); echo pid=$p; kill -0 $p 2>/dev/null && echo alive || echo dead; echo now=$(date +%s) hb=$(cat /data/local/tmp/urtv_tA.hb 2>/dev/null); ls -l /data/local/tmp/ | grep -E "urtv_t|home_(pid|hb|ok)"'
```
B. The same, started with a single command (not --batch):
```
optimize/venv/bin/python -I optimize/tv.py "nohup sh -c 'echo \$\$ > /data/local/tmp/urtv_tB.pid; i=0; while [ \$i -lt 40 ]; do date +%s > /data/local/tmp/urtv_tB.hb; sleep 3; i=\$((i+1)); done' > /dev/null 2>&1 &"; echo rcB=$?
sleep 8
optimize/venv/bin/python -I optimize/tv.py 'p=$(cat /data/local/tmp/urtv_tB.pid 2>/dev/null); echo pid=$p; kill -0 $p 2>/dev/null && echo alive || echo dead; echo now=$(date +%s) hb=$(cat /data/local/tmp/urtv_tB.hb 2>/dev/null)'
```
C. The same with setsid (detached from the adb shell):
```
optimize/venv/bin/python -I optimize/tv.py "setsid sh -c 'echo \$\$ > /data/local/tmp/urtv_tC.pid; i=0; while [ \$i -lt 40 ]; do date +%s > /data/local/tmp/urtv_tC.hb; sleep 3; i=\$((i+1)); done' < /dev/null > /dev/null 2>&1 &"; echo rcC=$?
sleep 8
optimize/venv/bin/python -I optimize/tv.py 'p=$(cat /data/local/tmp/urtv_tC.pid 2>/dev/null); echo pid=$p; kill -0 $p 2>/dev/null && echo alive || echo dead; echo now=$(date +%s) hb=$(cat /data/local/tmp/urtv_tC.hb 2>/dev/null); which setsid nohup; ls -l /data/local/tmp/deadman.sh'
```
D. Clean up (always):
```
optimize/venv/bin/python -I optimize/tv.py 'for f in /data/local/tmp/urtv_tA.pid /data/local/tmp/urtv_tB.pid /data/local/tmp/urtv_tC.pid; do p=$(cat $f 2>/dev/null); [ -n "$p" ] && kill $p 2>/dev/null; done; rm -f /data/local/tmp/urtv_t*; echo cleaned'
```
REPORT.md: "step 2 (diagnostic)", then the outputs of A, B, C, D as they are. Push. Then wait for step 3.

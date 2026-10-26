step 2 (diagnostic)

```
--- A
$ nohup sh -c 'echo $$ > /data/local/tmp/urtv_tA.pid; i=0; while [ $i -lt 40 ]; do date +%s > /data/local/tmp/urtv_tA.hb; sleep 3; i=$((i+1)); done' > /dev/null 2>&1 &

rcA=0
pid=
dead
now=1793033059 hb=
-rw-rw-rw- 1 shell shell        0 2026-10-23 23:30 home_ok
-rw-rw-rw- 1 shell shell        0 2026-10-26 19:16 home_ok.1793031163-61915-29745
--- B

rcB=0
pid=
dead
now=1793033069 hb=
--- C

rcC=0
pid=
dead
now=1793033078 hb=
/system/bin/setsid
/system/bin/nohup
-rw-rw-rw- 1 shell shell 1753 2026-10-26 19:15 /data/local/tmp/deadman.sh
--- D
cleaned
```

| # | OK/problem | shots | note |
|---|---|---|---|
| 2 | problem | — | A (--batch), B (single command) and C (setsid): rc=0 but no pid file and no heartbeat each time; the job never ran or died at once. setsid and nohup exist. D cleaned. |

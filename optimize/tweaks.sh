#!/usr/bin/env bash
# Speed and memory tweaks that are safe and reversible. Nothing is uninstalled or disabled here.
#   optimize/tweaks.sh apply     - animations at half length
#                                - Play Store kept from running in the background (it stays installed and opens
#                                  normally; on one set its idle CPU fell 31% -> 6%)
#                                - closed apps frozen while cached (no CPU in the background)
#                                - app caches trimmed, only when the free storage is low (under 1 GB or 10%): the
#                                  caches (thumbnails, app data to load) are useful while there is room
#   optimize/tweaks.sh restore   back to the values the TV had before (the journal optimize/run/tweaks-journal.txt, written
#                                by the first apply: a value that was not set is deleted, not written); a TV with no journal
#                                gets Android's defaults
#   optimize/tweaks.sh status    what each setting was and is now (the report shows it beside the measurements)
# Every value is read back after it is set: a TV that does not take it gets a «!!» line (the setup goes on).
# Not by default until it is measured again: CACHED_LIMIT=16 optimize/tweaks.sh apply also keeps at most 16 closed
# apps in memory (only lowered, never raised). It shows more free RAM, but Android 14 raised this limit on purpose
# (up to 30% fewer cold app starts, source.android.com/docs/core/perf/cached-apps-freezer), and frozen cached apps
# use no CPU anyway. Seen on a TCL Google TV (Android 14): 1024 at boot; the device_config value only takes effect
# after a restart and did not survive one there (null again), so `dumpsys activity settings` shows the boot value
# and `device_config get activity_manager max_cached_processes` the setting.
set -euo pipefail
cd "$(dirname "$0")/.."
PY="${PY:-optimize/venv/bin/python}"
CACHED="${CACHED_LIMIT:-}"
JOURNAL="${URTV_RUN_DIR:-optimize/run}/tweaks-journal.txt"
KEYS="window_animation_scale transition_animation_scale animator_duration_scale cached_apps_freezer max_cached_processes"
getcmd() { case "$1" in max_cached_processes) echo "device_config get activity_manager $1" ;; *) echo "settings get global $1" ;; esac; }
putcmd() {   # key value: a value that was not set (null) is deleted
  case "$1" in
    max_cached_processes) if [ "$2" = null ]; then echo "device_config delete activity_manager $1"; else echo "device_config put activity_manager $1 $2"; fi ;;
    *) if [ "$2" = null ]; then echo "settings delete global $1"; else echo "settings put global $1 $2"; fi ;;
  esac
}
tvget() { local v; v=$("$PY" -I optimize/tv.py "$(getcmd "$1")" | tr -d '\r' | tail -n 1); echo "${v:-null}"; }
jget() { sed -n "s/^$1=//p" "$JOURNAL" 2> /dev/null | head -n 1; }
# The Play Store's standby bucket and background appops are recorded and restored the same way (names: the bucket by name, the
# appops by mode; «default» when the TV has none set). A firmware that does not say gets «unknown» and today's values on restore.
PS=com.android.vending
PSKEYS="standby_bucket appop_RUN_IN_BACKGROUND appop_RUN_ANY_IN_BACKGROUND"
bucket_name() { case "$1" in 10) echo active ;; 20) echo working_set ;; 30) echo frequent ;; 40) echo rare ;; 45) echo restricted ;; 50) echo never ;; *) echo unknown ;; esac; }
psget() {
  local o
  case "$1" in
    standby_bucket) o=$("$PY" -I optimize/tv.py "am get-standby-bucket $PS" 2> /dev/null | tr -d '\r' | tail -n 1 | tr -dc '0-9') || o=""
                    bucket_name "$o" ;;
    appop_*) o=$("$PY" -I optimize/tv.py "appops get $PS ${1#appop_}" 2> /dev/null | tr -d '\r' | head -n 1) || o=""
             case "$o" in
               "No operations"*) echo default ;;
               "${1#appop_}: "*) o=${o#*: }; o=${o%%[;, ]*}; [[ "$o" =~ ^[a-z_]+$ ]] && echo "$o" || echo unknown ;;
               *) echo unknown ;;
             esac ;;
  esac
}
psput() {   # key value
  case "$1" in
    standby_bucket) echo "am set-standby-bucket $PS $2" ;;
    appop_*) echo "cmd appops set $PS ${1#appop_} $2" ;;
  esac
}
verify() {   # key wanted: read back; a «!!» line when the TV did not take it
  local got; got=$(tvget "$1")
  if [ "$got" != "$2" ]; then echo "!! $1: this TV did not take the value (asked for $2, it says $got)"; return 1; fi
}
case "${1:?usage: optimize/tweaks.sh apply|restore|status}" in
  apply)   v=0.5; bucket=restricted; bg=deny ;;
  restore) v=1.0; bucket=active;     bg=allow ;;   # (what a TV with no record gets; with one, its own values)
  status)
    [ -f "$JOURNAL" ] || { echo "Tweaks: none recorded (nothing was changed by optimize/tweaks.sh apply)"; exit 0; }
    for k in $KEYS; do echo "- tweak $k: was $(jget "$k"), now $(tvget "$k")"; done
    for k in $PSKEYS; do echo "- tweak $k: was $(jget "$k"), now $(psget "$k")"; done
    exit 0 ;;
  *) echo "usage: optimize/tweaks.sh apply|restore|status"; exit 1 ;;
esac
cmds=()
want=()   # key=value pairs to read back
pswant=() # the same for the Play Store's bucket and appops
if [ "$1" = apply ]; then
  # the first apply records what the TV had (a later one, as after run.sh again, keeps that record: the TV is not "as it came" then)
  if [ ! -f "$JOURNAL" ]; then
    mkdir -p "$(dirname "$JOURNAL")"
    { echo "# what the TV had before optimize/tweaks.sh apply (null: it was not set); optimize/tweaks.sh restore puts exactly this back"
      for k in $KEYS; do echo "$k=$(tvget "$k")"; done
      for k in $PSKEYS; do echo "$k=$(psget "$k")"; done; } > "$JOURNAL.tmp" && mv "$JOURNAL.tmp" "$JOURNAL"
  fi
  cmds+=("$(psput standby_bucket restricted)" "$(psput appop_RUN_IN_BACKGROUND deny)" "$(psput appop_RUN_ANY_IN_BACKGROUND deny)")
  pswant+=("standby_bucket=restricted" "appop_RUN_IN_BACKGROUND=deny" "appop_RUN_ANY_IN_BACKGROUND=deny")
  for k in window_animation_scale transition_animation_scale animator_duration_scale; do cmds+=("$(putcmd "$k" "$v")"); want+=("$k=$v"); done
  if [ -n "$CACHED" ]; then   # opt-in only (see above); never raise the limit: low-RAM sets may sit below it
    now=$("$PY" -I optimize/tv.py "dumpsys activity settings 2> /dev/null | grep -m1 max_cached_processes" | tr -dc '0-9')
    if [ -n "$now" ] && [ "$now" -gt "$CACHED" ]; then
      cmds+=("$(putcmd max_cached_processes "$CACHED")"); want+=("max_cached_processes=$CACHED")
      echo "Closed apps kept in memory: $now -> $CACHED"
    else
      echo "Closed apps kept in memory: ${now:-unknown} (left as is)"
    fi
  else
    cmds+=("$(putcmd max_cached_processes null)"); want+=("max_cached_processes=null")   # Android's own limit (undoes an older run; the journal has the original)
  fi
  cmds+=("$(putcmd cached_apps_freezer enabled)"); want+=("cached_apps_freezer=enabled")
  # trimming throws away every app's cache (thumbnails, things apps would load again): only worth it when storage is short
  space=$("$PY" -I optimize/tv.py "df -k /data 2> /dev/null | tail -n 1" | tr -d '\r' | awk '{print $2, $4}') || space=""
  read -r total avail <<< "$space" || true
  if [[ "${total:-}" =~ ^[0-9]+$ && "${avail:-}" =~ ^[0-9]+$ && "$total" -gt 0 ]]; then
    if [ "$avail" -lt 1048576 ] || [ $((avail * 10)) -lt "$total" ]; then
      cmds+=("pm trim-caches 999G")
      echo "Free storage is low ($((avail / 1024)) MB of $((total / 1024)) MB): app caches trimmed."
    else
      echo "Free storage is fine ($((avail / 1024)) MB of $((total / 1024)) MB): app caches left alone."
    fi
  else
    echo "Free storage unknown: app caches left alone."
  fi
elif [ -f "$JOURNAL" ]; then
  for k in $KEYS; do
    o=$(jget "$k")
    if [[ "$o" =~ ^(null|[A-Za-z0-9_.-]+)$ ]]; then cmds+=("$(putcmd "$k" "$o")"); want+=("$k=$o")
    else echo "!! $k: the recorded value is not one this script puts back; left as it is"; fi
  done
  for k in $PSKEYS; do
    o=$(jget "$k")
    if [[ "$o" =~ ^[a-z_]+$ ]] && [ "$o" != unknown ]; then cmds+=("$(psput "$k" "$o")"); pswant+=("$k=$o")
    else   # the original could not be read on this firmware: today's values, said
      d=allow; [ "$k" != standby_bucket ] || d=active
      echo "!! $k: the TV's own value was not recorded (this firmware does not say), so $d is put back"
      cmds+=("$(psput "$k" "$d")")
    fi
  done
  echo "Putting back the values the TV had before (optimize/run/tweaks-journal.txt)."
else
  cmds+=("$(psput standby_bucket active)" "$(psput appop_RUN_IN_BACKGROUND allow)" "$(psput appop_RUN_ANY_IN_BACKGROUND allow)")
  for k in window_animation_scale transition_animation_scale animator_duration_scale; do cmds+=("$(putcmd "$k" "$v")"); done
  cmds+=("$(putcmd max_cached_processes null)" "$(putcmd cached_apps_freezer device_default)")
  echo "No journal of the tweaks: Android's defaults."
fi
printf '%s\n' "${cmds[@]}" | "$PY" -I optimize/tv.py --batch > /dev/null
bad=0
for kv in "${want[@]+"${want[@]}"}"; do verify "${kv%%=*}" "${kv#*=}" || bad=1; done
for kv in "${pswant[@]+"${pswant[@]}"}"; do   # the standby bucket is the system's to move, so a mismatch is said, not fatal
  got=$(psget "${kv%%=*}")
  if [ "$got" != "${kv#*=}" ]; then echo "!! ${kv%%=*}: this TV did not take the value (asked for ${kv#*=}, it says $got)"; bad=1; fi
done
if [ "$1" = restore ] && [ -f "$JOURNAL" ] && [ "$bad" = 0 ]; then rm -f "$JOURNAL"; fi   # kept when the TV refused something
if [ "$1" = apply ]; then echo "Animation scale $v, Play Store background: $bg, cached-app freezer: on"
else echo "Tweaks undone, Play Store background: $bg"; fi

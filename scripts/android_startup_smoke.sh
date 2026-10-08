#!/usr/bin/env bash
set -euo pipefail

mapfile -t APKS < <(find apk -type f -name '*.apk')
test "${#APKS[@]}" -eq 1
adb install "${APKS[0]}"
adb logcat -c
adb shell am start -W -n io.github.aeee123.koitune.debug/moe.rukamori.archivetune.MainActivity | tee startup.txt
grep -q 'Status: ok' startup.txt
for attempt in 1 2 3 4; do
  sleep 5
  adb shell pidof io.github.aeee123.koitune.debug
done
adb logcat -b crash -d > crash-log.txt
! grep -Fq 'Process: io.github.aeee123.koitune.debug' crash-log.txt
adb exec-out screencap -p > startup.png
adb shell uiautomator dump /sdcard/window.xml
adb pull /sdcard/window.xml startup-window.xml

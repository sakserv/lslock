#!/usr/bin/env bash
jar="$1"

bash /lslock-test $jar &
sleep 3
bash /lslock-test $jar &
sleep 3

while [ $(cat /proc/locks | wc -l) -gt 0 ]; do
    bash /lslock $jar
done
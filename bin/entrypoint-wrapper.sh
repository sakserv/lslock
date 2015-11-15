#!/usr/bin/env bash
jar="$1"

bash /lslock-test $jar &
sleep 5
bash /lslock-test $jar &
sleep 5

while [ $(cat /proc/locks | wc -l) -gt 0 ]; do
    bash /lslock $jar
done
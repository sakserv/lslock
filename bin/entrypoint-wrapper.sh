#!/usr/bin/env bash
jar="$1"

bash /lslock-test $jar &
sleep 5
bash /lslock-test $jar &
sleep 5

i=5
while [ $i -gt 0 ]; do
    bash /lslock $jar
    sleep 5
    i=$(( i - 1 ))
done
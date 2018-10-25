#!/bin/bash
if [ ! -d target ] ; then
    mvn package
fi
java -cp target/multi-threaded-server-0.2.0.jar core.Server &
for i in $(seq 1 $1); do
    java -cp target/multi-threaded-server-0.2.0.jar core.Main &
done
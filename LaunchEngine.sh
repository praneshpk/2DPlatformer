#!/bin/bash
if [ ! -d target ] ; then
    mvn package -Dmaven.test.skip=true
fi
java -cp target/2DPlatformerEngine-0.4.0.jar core.Server &
for i in $(seq 1 $1); do
    sleep .5
    java -cp target/2DPlatformerEngine-0.4.0.jar core.Main &
done
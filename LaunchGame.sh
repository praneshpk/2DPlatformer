#!/bin/bash
if [ ! -d target ] ; then
    mvn package -Dmaven.test.skip=true
fi
java -cp target/2DPlatformer-0.3.0.jar core.Server &
for i in $(seq 1 $1); do
    java -cp target/2DPlatformer-0.3.0.jar core.Main &
done
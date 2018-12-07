#!/bin/bash
if [ ! -d target ] ; then
    mvn package -Dmaven.test.skip=true
fi
java -cp target/2DPlatformerEngine-0.4.0.jar core.scripts.ScriptingExample
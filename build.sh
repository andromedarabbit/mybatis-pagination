#!/usr/bin/env bash
./wait.sh && mvn clean -DskipTests=false -Dmaven.javadoc.skip=true -B -V install

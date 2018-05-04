#!/usr/bin/env bash
./wait.sh && ./mvnw clean -DskipTests=false -Dmaven.javadoc.skip=true -B -V install

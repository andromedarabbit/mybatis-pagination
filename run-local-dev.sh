#!/usr/bin/env bash

PROCESSES=$(docker ps -a -q --filter "name=jet-mariadb_local-")
if [ -n "$PROCESSES" ]; then
  docker rm "$(docker ps -a -q --filter 'name=jet-mariadb_local-')"
fi
unset PROCESSES

jet run mariadb_local

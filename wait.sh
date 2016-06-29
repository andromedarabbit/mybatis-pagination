#!/usr/bin/env bash


function test_mariadb {
  mysqladmin --protocol tcp -h localhost -uroot -pMyPassword ping
}

count=0
# Chain tests together by using &&
until ( test_mariadb )
do
  ((count++))
  if [ ${count} -gt 50 ]
  then
    echo "Services didn't become ready in time"
    exit 1
  fi
  sleep 1
done

exit 0

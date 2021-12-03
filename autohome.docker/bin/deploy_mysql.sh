#!/bin/bash

cd `dirname $0`

action=$1

help(){
  echo "输入错误，用法如下："
  echo "./deploy_mysql.sh init/start/stop/help"
}

init(){
  echo "init"
  docker build -t mysql-spider .
}

start(){
  echo "start"
  docker-compose up -d --force-recreate
}

stop(){
  echo "stop"
  docker-compose down
}

if [ "${action}" = "start" ] ;then
  start
elif [ "${action}" = "stop" ] ;then
  stop
else
  help
fi




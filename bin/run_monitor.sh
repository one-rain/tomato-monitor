#!/bin/bash
#run_monitor.sh

function sendEmail() {
    if [ $# != 2 ]; then
        display_help
        exit 0
    else
        curl -H "content-type: application/x-www-form-urlencoded; charset=UTF-8" -d "content=$2" http://127.0.0.1/ssports-alarm/alarm/send?tag=${1}&valid=1
    fi
}

function display_help() {
  cat <<EOF
  Error: parameter error. please check.

  Usage:
      ./bin/run_monitor.sh  service_tag content
  OPTIONS:
      service_tag            监控服务的标签
      content                报警内容

EOF
}

function main_help() {
cat <<EOF
  Error: parameter error. please check.

  Usage:
      ./bin/run_monitor.sh name
  OPTIONS:
      name            监控名称。如：mongo、oss、nodejs
EOF
}


function monitorNodejs() {
    result=$(curl -H "content-type: application/x-www-form-urlencoded; charset=UTF-8"  http://${1}:3000/service)
    echo "$result"
    if [ "$result" == '{"code":200}' ]; then
        echo "$1 Node.js service is ok."
    else
        echo "$1 Node.js service connection timeout."
        sendEmail "ssports-data-node" "Node.js服务出现异常，请检查！"
    fi
}

if [ "$1" == "mongo" ]; then
    info "monitor mongo"
elif [ "$1" == "oss" ]; then
    info "monitor oss"
elif [ "$1" == "nodejs" ]; then
    monitorNodejs "10.10.1.1"
    monitorNodejs "10.10.1.2"
else
    main_help
fi

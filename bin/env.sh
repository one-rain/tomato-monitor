#!/bin/bash

source ~/.bashrc

############################
#    basis config info     #
#    author: wangrun       #
############################

#JAVA_HOME=/usr/local/jdk1.8.0_101
JAVA_OPTS="-Xms100m -Xmx1024m"

function info() {
  if [ ${CLEAN_FLAG} -ne 0 ]; then
    local msg=$1
    echo "Info: $msg" >&2
  fi
}

function warn() {
  if [ ${CLEAN_FLAG} -ne 0 ]; then
    local msg=$1
    echo "Warning: $msg" >&2
  fi
}

function error() {
  local msg=$1
  local exit_code=$2

  echo "Error: $msg" >&2

  if [ -n "$exit_code" ] ; then
    exit $exit_code
  fi
}

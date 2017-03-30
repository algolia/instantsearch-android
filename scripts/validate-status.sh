#!/usr/bin/env bash
set -e # exit when error

currentBranch=`git rev-parse --abbrev-ref HEAD`
if [ $currentBranch != 'master' ]; then
  printf "Error: You must be on maste§r\n"
  exit 1
fi

if [[ -n $(git status --porcelain | grep -v '??') ]]; then
  printf "Error: Working tree is not clean (git status)\n"
  exit 1
fi

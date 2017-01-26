#!/usr/bin/env bash
set -e # exit when error

currentBranch=`git rev-parse --abbrev-ref HEAD`
if [ $currentBranch != 'master' ]; then
  printf "Release: You must be on maste§r\n"
  exit 1
fi

if [[ -n $(git status --porcelain) ]]; then
  printf "Release: Working tree is not clean (git status)\n"
  exit 1
fi

printf "\n\nRelease: update working tree"
git pull origin master
git fetch origin --tags

(cd docgen && NODE_ENV=production npm run build)
git add docs/
git commit -m "chore: Update docs"
git push origin master

#!/usr/bin/env bash
set -e # exit when error

currentBranch=`git rev-parse --abbrev-ref HEAD`
if [ $currentBranch != 'master' ]; then
  printf "Release: You must be on master\n"
  exit 1
fi

if [[ -n $(git status --porcelain | grep -v '??') ]]; then
  printf "Release: Working tree is not clean (git status)\n"
  exit 1
fi

printf "\n\nRelease: update working tree\n"
git pull origin master
git fetch origin --tags

git checkout androidX

printf "\n\nRelease: rebasing androidX on master\n"
git rebase master

printf "\n\nRelease: uploading androidX artifacts\n"
./gradlew bintrayUpload

git push origin androidX --force
echo "Success! New version of androidx artifacts has been released."
git checkout master

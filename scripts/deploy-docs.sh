#!/usr/bin/env bash
set -e # exit when error

currentBranch=`git rev-parse --abbrev-ref HEAD`
if [ $currentBranch != 'master' ]; then
  printf "Release: You must be on maste§r\n"
  exit 1
fi

if [[ -n $(git status --porcelain | grep -v '??') ]]; then
  printf "Release: Working tree is not clean (git status)\n"
  exit 1
fi

printf "\n\nRelease: update working tree\n"
git pull origin master
git fetch origin --tags

printf "\n\nRelease: generate and copy javadoc\n"
rm -rf docgen/src/javadoc/
./gradlew copyJavadoc
git add docgen/src/javadoc/

printf "\n\nRelease: build static documentation\n"
(cd docgen && NODE_ENV=production npm run build)
git add docs/
git commit -m "chore: Update docs"
git push origin master

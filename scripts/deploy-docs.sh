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
(cd docgen && npm install && NODE_ENV=production npm run build)

tmpdir=`mktemp -d 2>/dev/null || mktemp -d -t 'tmpdir'`
cp -r docs/* $tmpdir
git reset HEAD docs docgen
git checkout -- docs docgen
git checkout gh-pages
rm -rf *
cp -r $tmpdir/* .
echo "Replaced GH pages with newly generated docs"
git add .
git commit -m "chore: Update docs"
git push origin gh-pages
echo "Success! New version of docs has been published on GH Pages."
git checkout master

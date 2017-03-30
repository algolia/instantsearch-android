#!/usr/bin/env bash
set -e # exit when error
./scripts/validate-status.sh

printf "\n\nRelease: update working tree"
git pull origin master
git fetch origin --tags

(cd docgen && NODE_ENV=production npm run build)
git add docs/
git commit -m "chore: Update docs"
git push origin master

#! /usr/bin/env bash
set -e # Exit on errors
./scripts/validate-status.sh

./gradlew copyJavadoc
git add docgen/src/javadoc/

(cd docgen && NODE_ENV=production npm run build)
git add docs/
git commit -m "docs(javadoc): Update and deploy javadoc"
git push origin master


#!/usr/bin/env bash

set -eo pipefail

if [ $# -ne 1 ]; then
    echo "$0 | A script to release new versions automatically"
    echo "Usage: $0 VERSION_CODE"
    exit -1
fi

SELF_ROOT=$(cd "$(dirname "$0")" && pwd)
FILE_GRADLE_PROPERTIES="$SELF_ROOT/properties.gradle"
FILE_GETTING_STARTED="$SELF_ROOT/docgen/src/getting-started.md"
FILE_README="$SELF_ROOT/README.md"
VERSION_CODE=$1
CHANGELOG="CHANGELOG.md"

set +eo pipefail
COUNT_DOTS=$(grep -o "\." <<< $VERSION_CODE | wc -l)
set -eo pipefail

if [ $COUNT_DOTS -ne 2 ]; then
    echo "$VERSION_CODE is not a valid version code, please use the form X.Y.Z (e.g. v1 = 1.0.0)"
    exit -1
fi


# Check that the working repository is clean (without any changes, neither staged nor unstaged).
# An exception is the change log, which should have been edited, but not necessarily committed (we usually commit it
# along with the version number).
if [[ ! -z $(git status --porcelain | grep -v "$CHANGELOG") ]]; then
    echo "ERROR: Working copy not clean! Aborting." 1>&2
    echo "Changes: $(git status)"
    echo "Please revert or commit any pending changes before releasing." 1>&2
    exit 1
fi

# Check that the change log contains information for the new version.
set +e
version_in_change_log=$(grep -E "^#+" "$SELF_ROOT/$CHANGELOG" | sed -E 's/^#* ([0-9.]*)\s*.*$/\1/g' | grep -x "$VERSION_CODE")
set -e
if [[ -z $version_in_change_log ]]; then
    echo "Version $VERSION_CODE not found in change log! Aborting." 1>&2
    exit 2
fi

# Only release on master (for manual runs, cannot happen through fastlane)
currentBranch=$(git rev-parse --abbrev-ref HEAD)
if [ "$currentBranch" != 'master' ]; then
  printf "Release: You must be on master\\n"
  exit 1
fi

function call_sed(){
PATTERN="$1"
FILENAME="$2"

# Mac needs a space between sed's inplace flag and extension
if [ "$(uname)" == "Darwin" ]; then
    sed -E -i '' "$PATTERN" "$FILENAME"
else
    sed -E -i "$PATTERN" "$FILENAME"
fi
}

echo "Updating version number to $VERSION_CODE..."
call_sed "s/VERSION = '.*'/VERSION = '$VERSION_CODE'/"              "$FILE_GRADLE_PROPERTIES"
call_sed "s/^(implementation '[^[:digit:]]+).*$/\1$VERSION_CODE'/"  "$FILE_GETTING_STARTED"
call_sed "s/^(implementation '[^[:digit:]]+).*$/\1$VERSION_CODE'/gm"  "$FILE_README"

# Commit to git
git add .
git commit -m "chore(release): Version $VERSION_CODE [ci skip]"

# Release on Bintray
./gradlew clean bintrayUpload

# Tag and push on GitHub
git tag "$VERSION_CODE" -s -m "v$VERSION_CODE"
git push origin $VERSION_CODE HEAD

# Update documentation
./scripts/deploy-docs.sh

# Update AndroidX artifacts
./scripts/release-androidx.sh

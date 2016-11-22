#!/usr/bin/env bash

set -eo pipefail

SELF_ROOT=$(cd $(dirname "$0") && pwd)
FILE_BUILD_GRADLE="$SELF_ROOT/instantsearch/build.gradle"
CHANGELOG="CHANGELOG.md"

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

if [ $# -ne 1 ]; then
    echo "$0 | A script to release new versions automatically"
    echo "Usage: $0 VERSION_CODE"
    exit -1
fi

VERSION_CODE=$1

# Check that the working repository is clean (without any changes, neither staged nor unstaged).
# An exception is the change log, which should have been edited, but not necessarily committed (we usually commit it
# along with the version number).
if [[ ! -z `git status --porcelain | grep -v "$CHANGELOG" | grep -v "^?? "` ]]; then
    echo "ERROR: Working copy not clean! Aborting." 1>&2
    echo "Please revert or commit any pending changes before releasing." 1>&2
    exit 1
fi

# Check that the change log contains information for the new version.
set +e
version_in_change_log=$(cat "$SELF_ROOT/$CHANGELOG" | grep -E "^#+" | sed -E 's/^#* ([0-9.]*)\s*.*$/\1/g' | grep -x "$VERSION_CODE")
set -e
if [[ -z $version_in_change_log ]]; then
    echo "Version $VERSION_CODE not found in change log! Aborting." 1>&2
    exit 2
fi

echo "Updating version number to $VERSION_CODE..."
call_sed "s/VERSION = '.*'/VERSION = '$VERSION_CODE'/" $FILE_BUILD_GRADLE

# Commit to git
set +e # don't crash if already committed
git add .
git commit -m "Version $VERSION_CODE"
set -e

# Release on Bintray
./gradlew clean build bintrayUpload

# Tag and push on GitHub
git tag $VERSION_CODE
git push --follow-tags

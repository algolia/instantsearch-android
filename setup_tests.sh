#!/bin/bash

set -e
set -o pipefail

if [ -z "$FILE" ];
then
    FILE="instantsearch/src/test/java/com/algolia/instantsearch/Helpers.java"
fi
echo "Helper file: $FILE."

if [[ ! -z `git status --porcelain | grep "$FILE" | grep -v "^?? "` ]]; then
    echo "ERROR: Helper file has been edited! Aborting." 1>&2
    echo "Please revert or commit any pending changes before running tests." 1>&2
    exit 1
fi

echo "Replacing environment variables..."
sed -i '' "s/APP_ID_REPLACE_ME/${ALGOLIA_APPLICATION_ID}/g" $FILE
sed -i '' "s/API_KEY_REPLACE_ME/${ALGOLIA_API_KEY}/g" $FILE
sed -i '' "s/JOB_NUMBER_REPLACE_ME/${TRAVIS_JOB_NUMBER}/g" $FILE

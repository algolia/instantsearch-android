#!/bin/bash

set -e
set -o pipefail

if [ -z "$FILE" ];
then
    FILE="instantsearch/src/test/java/com/algolia/instantsearch/Helpers.java"
fi
echo "Helper file: $FILE."
cp $FILE $FILE.bak

echo "Replacing environment variable..."
sed -i.tmp "s/APP_ID_REPLACE_ME/${ALGOLIA_APPLICATION_ID}/g" $FILE
sed -i.tmp "s/API_KEY_REPLACE_ME/${ALGOLIA_API_KEY}/g" $FILE
sed -i.tmp "s/JOB_NUMBER_REPLACE_ME/${TRAVIS_JOB_NUMBER}/g" $FILE

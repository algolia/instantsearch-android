#!/bin/bash

set -e
set -o pipefail

if [ -z "$FILE" ];
then
    FILE="instantsearch/src/test/java/com/algolia/instantsearch/Helpers.java"
fi

echo "Restoring Helper file..."
mv $FILE.bak $FILE
rm $FILE.tmp

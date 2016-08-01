#!/bin/bash

set -e
set -o pipefail

FILE=instantsearch/src/test/java/com/algolia/instantsearch/Helpers.java
export FILE

echo "Running Android test..."
./setup_tests.sh
./gradlew testRelease
./teardown_tests.sh

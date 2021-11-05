#/bin/sh

BUILD_DIR=build/generated/source/proto/main/kotlin
mkdir -p $BUILD_DIR
protoc --pbandk_out=$BUILD_DIR src/main/proto/com/algolia/instantsearch/telemetry/telemetry.proto

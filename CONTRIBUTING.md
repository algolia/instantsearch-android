[![InstantSearch android logo][logo]][website]

# Contributing Guide

- [Development](#development)
- [Code](#code)
- [Test](#test)
- [Lint](#lint)
- [Release](#release)
- [Update docs](#update-docs)

----
A note about the branches used in this project:
 - `develop` is for the major version in active development
 - `master` is for the main current stable version

You should do the dev and PR according to the target version. No new features
will be implemented on the master version.

## Development

You can use our [example applications](https://github.com/algolia/instantsearch-android-examples) while developing the library to test the new changes.

```sh
git clone git@github.com:algolia/instantsearch-android-examples.git
cd instantsearch-android-examples
gradle installDebug
```

### Using your local version of an IS module (core/ui) in a project

To use your local codebase instead of a published version:

1. In your app project, add the following to `settings.gradle`:
```groovy
include ':is'
project(':is').projectDir = new File(settingsDir, "../instantsearch-android/ui")
include ':core'
project(':core').projectDir = new File(settingsDir, "../instantsearch-android/core")
```
2. In your app's **`build.gradle`**, add `implementation project(":is")` (remove your existing dependency on a released version of IS if any)

**You should now be able to build your application with the local source of IS.**

## Code

The code for InstantSearch Android is located in [core.src](core/src) and [ui.src](ui/src).

## Tests

We have unit tests written with [JUnit](https://developer.android.com/studio/test/index.html):

To run the tests, set your environment with your Algolia credentials and run the script. **Since the tests are creating and removing indices, DO NOT use your production application.**
```sh
ALGOLIA_APPLICATION_ID=XXXX
ALGOLIA_API_KEY=XXXX
./run_tests.sh
```


[logo]: ./docgen/assets/img/instant-search-android.png
[website]: https://community.algolia.com/instantsearch-android

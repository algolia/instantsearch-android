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

## Code

The code for InstantSearch Android is located in [src](instantsearch/src).

## Test

We have unit tests written with [JUnit](https://developer.android.com/studio/test/index.html):

To run the tests, set your environment with your Algolia credentials and run the script: 
```sh
ALGOLIA_APPLICATION_ID=XXXX
ALGOLIA_API_KEY=XXXX
./run_tests.sh
```

[logo]: ./docgen/assets/img/instant-search-android.png
[website]: https://community.algolia.com/instantsearch-android

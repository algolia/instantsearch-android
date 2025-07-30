# Migrate from 2.x to 3.x

The library version `2.0` uses Kotlin version `1.6` and Algolia Kotlin API Client `2.0`. Below are the steps to migrate.

> This new version removes all deprecated methods and features from `v1`.

## Table of Contents

- [InstantSearch Android Package](#instantsearch-android-package)
- [Kotlin API Client](#kotlin-api-client)
- [Searchers](#searchers)
- [Extension modules](#extension-modules)

## InstantSearch Android Package

InstantSearch Android package has changed. Please update your imports accordingly:

| Module                | 2.x                                        | 3.x                                 |
|-----------------------|:-------------------------------------------|:------------------------------------|
| InstantSearch Android | `com.algolia.instantsearch.helper.android` | `com.algolia.instantsearch.android` |

## Kotlin API Client

Kotlin API client (and its underlying Ktor client) is part of the library's binary interface. Please follow
its [migration guide][1], or apply the following changes:

### LogLevel

The library uses `LogLevel` from the Kotlin API client instead of Ktor's `Loglevel`:

| Subsystem   | 2.x                                        |                  3.x                  |
|-------------|:-------------------------------------------|:-------------------------------------:|
| Loglevel    | `io.ktor.client.features.logging.LogLevel` | `com.algolia.search.logging.LogLevel` |

### Public constants

Constants (e.g. `KeyIndexName`, `KeyEnglish` and `RouteIndexesV1`) are not exposed anymore. Please use your constants
instead.

### Ktor client

Refer to Ktor's [migration guide](https://ktor.io/docs/migrating-2.html#feature-plugin-client).

## Searchers

Legacy searchers are removed; please migrate as follows:

| Searcher              | 2.x                     | 3.x              |
|-----------------------|:------------------------|:-----------------|
| Single Index Searcher | `SearcherSingleIndex`   | `HitsSearcher`   |
| Facets Searcher       | `SearcherForFacets`     | `FacetsSearcher` |
| Multi Index Searcher  | `SearcherMultipleIndex` | `MultiSearcher`  |

## Extension modules

Multiple extensions have been extracted to modules:

| Subsystem                   | Module                                      |
|-----------------------------|:--------------------------------------------|
| Androidx SwipeRefreshLayout | `com.algolia:instantsearch-android-loading` |
| Androidx Paging 3           | `com.algolia:instantsearch-android-paging3` |
| Androidx Paging 2           | _Removed_                                   |

[1]: https://github.com/algolia/algoliasearch-client-kotlin/blob/master/docs/guide/Migrate_1.x_2.x.md

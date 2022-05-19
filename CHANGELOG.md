# 3.0.0

### Added
* Conditional search: `triggerSearchFor` param for hits and facets searchers
* Searchers: `connectHitsView` optional `past` parameter
* Extension function(s) `setBoundsFromFacetStats` to `FilterComparisonConnector`
* Extension modules:
    * `instantsearch-android-paging3` supporting [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
    * `instantsearch-android-loading` as separate module for [SwipeRefreshLayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout)
    * `instantsearch-coroutines-extensions`: coroutines extensions for Instant Search

### Changed
* Instant Search Android package from `com.algolia.instantsearch.helper.android` to `com.algolia.instantsearch.android`
* Promote experimental Searchers (`MultiSearcher`, `HitsSearcher` and `FacetsSearcher`)  to stable
* Searchers' `search()` [main safe](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#main-safe)
* Compose `SearchBox`: update widget params
* Update Kotlin to `1.6.10`
* Update Algolia API Kotlin Client to [2.0](https://github.com/algolia/algoliasearch-client-kotlin/releases/tag/2.0.0)
* Update Ktor Http Client to [2.0](https://blog.jetbrains.com/ktor/2022/04/11/ktor-2-0-released/)

### Fixed
* Facets searcher: `setQuery` function to set `facetQuery` field

### Removed
* Paging 2 support
* Deprecated searchers `SearcherMultipleIndex`, `SearcherSingleIndex` and `SearcherForFacets`

# 2.11.4

### Added
- *Experimental*: `MultiSearch`, `HitsSearcher` and `FacetsSearcher`
- Telemetry collection

### Changed
- Ktor `1.6.7`
- atomicfu `0.16.3`
- androidx core `1.7.0`
- androidx appcompat `1.4.0`

# 2.11.3

### Changed
- Ktor `1.6.6`

### Fixed
- `instantsearch-android` AAR artifact publish

# 2.11.2

### Fixed
- Android 12 support: update `work-runtime` to `2.7.1`

### Changed
- Compile target to Android 12 (API level 31) 
- Kotlin API Client `1.12.0`
- Kotlin `1.5.31`
- Compose `1.0.5`

# 2.11.1

### Added
- `RelevantSortPriorityState`: convenience alias of `RelevantSortState<RelevantSortPriority?>` (#254)

### Fixed
- Ignore insights events older than 4 days (#257)
- Searcher single index (v2) paging (#260)

### Changed
- compose: mark `Paginator` as experimental 

# 2.11.0

### Added
- InstantSearch Compose (#243)
- Dynamic Facet List (#244)

### Fixed
- Unexpected behaviour of paging sorted index (#234)

### Changed
- Kotlin `1.5.21`
- Kotlin Coroutines `1.5.0`
- Ktor `1.6.2`

# 2.10.0

### Added
- Integration of `InstantSearch Insights` library
- Add `retry` to paging data sources

# 2.9.0

### Added
- Relevant sort widget (#229)

### Changed
- Algolia Kotlin Client `1.7.0`
- Kotlin `1.4.31`
- Ktor `1.5.2`

### Fixed
- `Subscription`: subscriptions list thread-safety

# 2.8.0

### Changed
- Kotlin `1.4.21`
- Kotlin Coroutines `1.4.2`
- Ktor `1.5.1`

### Experimental
- Add support for answers search client and IS widgets

# 2.7.3

### Added
- Enable/disable loading in PagedList's dataSource (#220)

# 2.7.2

### Changed
- Algolia Kotlin Client `1.5.2`

# 2.7.1

### Changed
- Algolia Kotlin Client `1.5.1`
- Kotlin Coroutines `1.4.0`
- Ktor `1.4.1`
- Ktor Http engine to OkHttp

# 2.7.0

### Added
- Query rule custom data widget

# 2.6.0

### Changed
- Kotlin `1.4.10`
- Algolia Kotlin Client `1.5.0`
- Android minimum SDK to 21

# 2.5.1

### Fixed
- Filter unexpected items from the hierarchical tree results

# 2.5.0

### Added
- Support and target Android API 30
- Add selection capability to the hierarchical tree nodes
- Filter range dynamic behavior based on a searcher state

# 2.4.0

### Added
- Related items widget
- Connecting `SortByViewModel` to `PagedList`

### Changed
- Kotlin `1.3.72`
- Android Gradle Plugin `3.6.3`
- Algolia Kotlin Client `1.4.0` (#198)

### Fixed
- Apply correct spans in `toSpannedString` extension function

# 2.3.1

- Fix PagedList Bug when typing too fast #194

# 2.3.0

- Use Kotlin client `1.3.1`
- Updated Ktor to `1.3.0`

# 2.2.2

- Use Kotlin client `1.2.2`

# 2.2.1

- Use Kotlin client `1.2.1`
- Add `clear` method to `ConnectionHandler`

# 2.2.0

- Use Kotlin client `1.2.0` and its `-android` artifact.
- Updated Kotlin to `1.3.60`
- AutoCompleteTextView integration as a SearchBox #187
- Add connectFilterState with `SearcherMultipleIndex` #182

# 2.1.0

- Added a method `SearcherMultipleIndex.connectFilterState` #182

# 2.0.3

- Fix an issue with `SearcherSingleIndexDataSource` and `SearcherMultipleIndexDataSource` where responses and errors
were not dispatched to the main thread.

# 2.0.2

- Updated Kotlin client to `1.1.4`
- Updated Kotlin to `1.3.50`
- Updated Ktor to `1.2.4` (With serialization `0.12.0)
- Added extension function `Facet.toFilter()` to transform a `Facet` to a `Filter.Facet`

# 2.0.1

- Fixed unhandled exceptions in `Searcher` and `Debouncer`
- Use `advancedSearch` for better `Hierarchical` logic
- Optional `submitQuery` for `SearchBoxView`
- Add `SearcherPlaces`

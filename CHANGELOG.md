# Unreleased

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

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

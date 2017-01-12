# Changelog

## 0.5.3 (2017-01-12)

### Improvements
- When loading an image, fit and center it in its ImageView
- Better error message when the user forgot to give an identifier to a View

## 0.5.2 (2017-01-05)

### Fixes
- Fix NPE when binding to ProgressBar an attribute that is missing from some records
- In Hits, Log error instead of using a Toast
- Fix an erroneous tag when logging a specific error

### Improvements
- Use `IdRes` annotation when relevant
- Various improvements to the library's Javadoc

## 0.5.1 (2016-12-16)

### Fixes
- Fix #1: filters manually set with `Searcher.setQuery(new Query.setFilters("..."))` are no more deleted when using `RefinementList` or `NumericRefinement`
- Fix `Stats` widget's handling of null query/error message
- Log error instead of throwing an exception when a request should have been cancelled

## 0.5.0 (2016-12-01)

### Features
- Faster and more robust Image Loading with [Glide](https://github.com/bumptech/glide)
- Support of binding to attributes that are missing from some records

### Fixes
- Correct Highlighter's usage of JSONPath
- Unify Searcher's constructor behavior
- Keep initial placeholders of ImageViews bound to a Hits widget for recycling
- Fix potential `Multiple dex files define Lorg/objectweb/asm/AnnotationVisitor` build error

## 0.4.0 (2016-11-24)

### Features
- Performance improvements of attributes binding
- Implement binding of values inside array attributes

### Fixes
- Correct location of Manifest for unit-tests
- Fill missing documentation of some public constants/members/classes

## 0.3.1 (2016-11-22)

### Fixes
- Correct binding of nested objects' values
- Report InstantSearch version in user-agent

## 0.3.0 (2016-11-22)

### Features
- Highlighting of nested objects
- Closing the SearchBox does not reset the interface anymore

### Fixes
- Highlighter now returns raw attribute value when no highlight exists
- Searcher only cancels once each former request when a new one is made
- Log messages are now prefixed with "Algolia|"
- Centered SearchBox's progress indicator

## 0.2.2 (2016-10-21)
- Remove `popup_filter.xml` from library

## 0.2.1 (2016-10-20)
- Move AlgoliaResultsListener out of the .ui. package

## 0.2.0 (2016-10-17)
- New package structure

## 0.1.0 (2016-10-15)
Initial release of InstantSearch Android.

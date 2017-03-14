# Changelog

## 0.7.0 (2017-03-14)

### Features

- New `AlgoliaFacetFilter` interface to define filter Views ([16d024a](https://github.com/algolia/instantsearch-android/commit/16d024a))
* **Filters:** New filter - NumericSelector ([4ddf599](https://github.com/algolia/instantsearch-android/commit/4ddf599))
* **Filters:** Implement autoHide for NumericSelector, refact to prevent duplication ([4740a69](https://github.com/algolia/instantsearch-android/commit/4740a69))
* **NumericRefinement:** Function for getting refinement operator given its name ([39be9b9](https://github.com/algolia/instantsearch-android/commit/39be9b9))
* **NumericSelector:** accept a non-filtering option ([c73874e](https://github.com/algolia/instantsearch-android/commit/c73874e))
* **Toggle:** New widget - toggle facet filter ([325b42e](https://github.com/algolia/instantsearch-android/commit/325b42e))
* **Toggle:** Setter for template, applied with last results ([94e60ec](https://github.com/algolia/instantsearch-android/commit/94e60ec))
* **Toggle:** Specialize as OneValueToggle ([7cc573e](https://github.com/algolia/instantsearch-android/commit/7cc573e))
* **Toggle:** Specialize as TwoValuesToggle ([301a854](https://github.com/algolia/instantsearch-android/commit/301a854))
* **Toggle:** template ([4fc49bb](https://github.com/algolia/instantsearch-android/commit/4fc49bb))

### Bug Fixes

* **Filters:** Throw if a filter misses an attribute ([d1a8b4a](https://github.com/algolia/instantsearch-android/commit/d1a8b4a))
* **InstantSearchHelper:** Never register twice a widget ([3c9560d](https://github.com/algolia/instantsearch-android/commit/3c9560d))
* **LayoutViews:** Ensure no null rootView is passed ([b43f000](https://github.com/algolia/instantsearch-android/commit/b43f000))
* **NumericSelector:** correct error message ([650321c](https://github.com/algolia/instantsearch-android/commit/650321c))
* **Toggle:** updating both value and name ([24bc640](https://github.com/algolia/instantsearch-android/commit/24bc640))
* **Toggle:** Version second TypedArray creation ([a9f8fe2](https://github.com/algolia/instantsearch-android/commit/a9f8fe2))

## 0.6.0 (2017-01-31)

### Features
- You can now use the `InstantSearchHelper` and the Widget system even if you don't have any `SearchView` ([e462661](https://github.com/algolia/instantsearch-android/commit/e462661))

### Fixes
- **SearchBox:** Fix iconifiedByDefault usage - we now use the current attribute's value if it is set, and default to false otherwise ([ac8ff47](https://github.com/algolia/instantsearch-android/commit/ac8ff47))

### Improvements
- We declared all data-binding attributes to improve your developing experience ([4d8fd3d](https://github.com/algolia/instantsearch-android/commit/4d8fd3d))

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

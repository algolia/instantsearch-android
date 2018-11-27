# Changelog

## 1.14.1
### Bug Fixes
* **README:** use 1.13.0 until core is in Jcenter ([f4cbf0e](https://github.com/algolia/instantsearch-android/commit/f4cbf0e))
* **release:** Use SSH format for REPO url [ci skip] ([6679e69](https://github.com/algolia/instantsearch-android/commit/6679e69))
* Downgrade gradle to last stable ([2017436](https://github.com/algolia/instantsearch-android/commit/2017436))
### Features
* **release:** Release AndroidX artifacts too ([0faa709](https://github.com/algolia/instantsearch-android/commit/0faa709))

## 1.14.0
* **Fastfile:** Adapt for new changes ([0294bc4](https://github.com/algolia/instantsearch-android/commit/0294bc4))
* **javadoc:** Stop using deprecated API ([be0787f](https://github.com/algolia/instantsearch-android/commit/be0787f))
* **NumericRefinementTest:** use right operator ([67bbefe](https://github.com/algolia/instantsearch-android/commit/67bbefe))
* **release.sh:** Update version in readme ([f5a40a3](https://github.com/algolia/instantsearch-android/commit/f5a40a3))
* **ui:** Add missing BASENAME ([3f3c88d](https://github.com/algolia/instantsearch-android/commit/3f3c88d))
* **UI:** tests extending base class ([25bea08](https://github.com/algolia/instantsearch-android/commit/25bea08))
### Features
* **gradle:** multi-module release, split logic into ext/libraries/release/standalone ([a858eec](https://github.com/algolia/instantsearch-android/commit/a858eec))
* **RefinementList:** When tied, sort ascending alpha ([7e3a88d](https://github.com/algolia/instantsearch-android/commit/7e3a88d))
* **release:** Update version in getting started ([8ea3d35](https://github.com/algolia/instantsearch-android/commit/8ea3d35))

## 1.13.0
### Bug Fixes
* **gradle:** Expose EventBus ([#93](https://github.com/algolia/instantsearch-android/issues/93)) ([9541eb1](https://github.com/algolia/instantsearch-android/commit/9541eb1))

## 1.12.1
### Features
* **VoiceUI:** migrate to library ([eaa7a43](https://github.com/algolia/instantsearch-android/commit/eaa7a43))

## 1.12.0
### Features
* **searchclient:** add searchDisjunctiveFaceting and searchForFacetValues ([80df479](https://github.com/algolia/instantsearch-android/commit/80df479))
* **searchclient:** Improve the searchClient by abstracting away the map functions. ([fc3eb25](https://github.com/algolia/instantsearch-android/commit/fc3eb25))

## 1.11.0
### Bug Fixes
* **Hits:** Use {pre,suf}fixes when highlighting attributes ([89e5333](https://github.com/algolia/instantsearch-android/commit/89e5333))

## 1.10.0
### Bug Fixes
* **RenderingHelper:** Solve memory leak by only keeping view IDs ([88af006](https://github.com/algolia/instantsearch-android/commit/88af006))
* **Searcher:** setFacet logic error ([42828de](https://github.com/algolia/instantsearch-android/commit/42828de))
### Features
* **Stats:** Accept HTML markup, handle empty view usage ([dd9ae9f](https://github.com/algolia/instantsearch-android/commit/dd9ae9f))

## 1.9.0
### New features
* **Searcher:** setFacet to change facet disjunctiveness
  ([979156b](https://github.com/algolia/instantsearch-android/commit/979156b))

### Bug Fixes

* **DataBinding:** update to v2
  ([77da772](https://github.com/algolia/instantsearch-android/commit/77da772))
  * **Hits:** don't process bindings when they are none
    ([dc3b11f](https://github.com/algolia/instantsearch-android/commit/dc3b11f))

## 1.8.2

## 1.8.1
### Bug Fixes
* **highlightColor:** typo in BindingAdapter ([69e8756](https://github.com/algolia/instantsearch-android/commit/69e8756))

## 1.8.0
### Bug Fixes
* **attrs:** Specify operator as enum for autocompletion ([67e57a5](https://github.com/algolia/instantsearch-android/commit/67e57a5))
* **Highlighter:** Restore highlighting several parts, better error handling ([b19ddd4](https://github.com/algolia/instantsearch-android/commit/b19ddd4))
* **Hits:** skip binding of attributes when the value is missing ([a371009](https://github.com/algolia/instantsearch-android/commit/a371009))
* **Hits:** Skip views that are not databound ([3d24f23](https://github.com/algolia/instantsearch-android/commit/3d24f23))
* **QuerySuggestionsContentProvider:** Don't suggest same query ([6e40984](https://github.com/algolia/instantsearch-android/commit/6e40984))
### Features
* **Highlighter:** highlighting with Bold ([d9161f3](https://github.com/algolia/instantsearch-android/commit/d9161f3))
* **Highlighter:** Inverted highlighting ([88874a2](https://github.com/algolia/instantsearch-android/commit/88874a2))
* **QSCP:** Highlighting through shouldReturnHighlightResult ([d7e6efa](https://github.com/algolia/instantsearch-android/commit/d7e6efa))
* **QuerySuggestions:** Inverted highlighting of results ([59efd20](https://github.com/algolia/instantsearch-android/commit/59efd20))
* **QuerySuggestionsContentProvider:** First draft ([24cb399](https://github.com/algolia/instantsearch-android/commit/24cb399))
* **views:** QuerySuggestions widget ([302c8ea](https://github.com/algolia/instantsearch-android/commit/302c8ea))

## 1.7.25

## 1.7.24

## 1.7.23

## 1.7.22

## 1.7.21

## 1.7.20

## 1.7.19

## 1.7.18

## 1.7.17

## 1.7.16

## 1.7.15
### Bug Fixes
* **dependencies:** make client an api ([c1200e0](https://github.com/algolia/instantsearch-android/commit/c1200e0))

## 1.7.14

## 1.7.13

## 1.7.12

## 1.7.11

## 1.7.10

## 1.7.9

## 1.7.8

## 1.7.7
### Bug Fixes
* **BindingHelper:** suffix handling ([2984fc7](https://github.com/algolia/instantsearch-android/commit/2984fc7))
* **BindingHelper:** suffix handling ([#75](https://github.com/algolia/instantsearch-android/issues/75)) ([7221b69](https://github.com/algolia/instantsearch-android/commit/7221b69))
* **dependencies:** Expose eventbus and glide apis ([cc49150](https://github.com/algolia/instantsearch-android/commit/cc49150))
* **gradle:** Add auto-minor version instead of fix ([523feaa](https://github.com/algolia/instantsearch-android/commit/523feaa))
* **release:** Add extra space in the fastfile changelog ([fc8d48b](https://github.com/algolia/instantsearch-android/commit/fc8d48b))
* **searcher:** Fix naming for index.getName() ([a020631](https://github.com/algolia/instantsearch-android/commit/a020631))
### Features
* **custombackend:** Change the Searcher to use a searchable index instead of concrete implementation ([#73](https://github.com/algolia/instantsearch-android/issues/73)) ([b070ad1](https://github.com/algolia/instantsearch-android/commit/b070ad1))

## 1.7.6
### Bug Fixes
* **BindingHelper:** suffix handling ([2984fc7](https://github.com/algolia/instantsearch-android/commit/2984fc7))
* **BindingHelper:** suffix handling ([#75](https://github.com/algolia/instantsearch-android/issues/75)) ([7221b69](https://github.com/algolia/instantsearch-android/commit/7221b69))
* **dependencies:** Expose eventbus and glide apis ([cc49150](https://github.com/algolia/instantsearch-android/commit/cc49150))
* **gradle:** Add auto-minor version instead of fix ([523feaa](https://github.com/algolia/instantsearch-android/commit/523feaa))
* **release:** Add extra space in the fastfile changelog ([fc8d48b](https://github.com/algolia/instantsearch-android/commit/fc8d48b))
* **searcher:** Fix naming for index.getName() ([a020631](https://github.com/algolia/instantsearch-android/commit/a020631))
### Features
* **custombackend:** Change the Searcher to use a searchable index instead of concrete implementation ([#73](https://github.com/algolia/instantsearch-android/issues/73)) ([b070ad1](https://github.com/algolia/instantsearch-android/commit/b070ad1))

## 1.7.5
### Bug Fixes
* **BindingHelper:** suffix handling ([2984fc7](https://github.com/algolia/instantsearch-android/commit/2984fc7))
* **BindingHelper:** suffix handling ([#75](https://github.com/algolia/instantsearch-android/issues/75)) ([7221b69](https://github.com/algolia/instantsearch-android/commit/7221b69))
* **dependencies:** Expose eventbus and glide apis ([cc49150](https://github.com/algolia/instantsearch-android/commit/cc49150))
* **gradle:** Add auto-minor version instead of fix ([523feaa](https://github.com/algolia/instantsearch-android/commit/523feaa))
* **release:** Add extra space in the fastfile changelog ([fc8d48b](https://github.com/algolia/instantsearch-android/commit/fc8d48b))
* **searcher:** Fix naming for index.getName() ([a020631](https://github.com/algolia/instantsearch-android/commit/a020631))
### Features
* **custombackend:** Change the Searcher to use a searchable index instead of concrete implementation ([#73](https://github.com/algolia/instantsearch-android/issues/73)) ([b070ad1](https://github.com/algolia/instantsearch-android/commit/b070ad1))

## 1.7.4
### Bug Fixes
* **BindingHelper:** suffix handling ([2984fc7](https://github.com/algolia/instantsearch-android/commit/2984fc7))
* **BindingHelper:** suffix handling ([#75](https://github.com/algolia/instantsearch-android/issues/75)) ([7221b69](https://github.com/algolia/instantsearch-android/commit/7221b69))

## 1.7.3
### Bug Fixes
* **gradle:** Add auto-minor version instead of fix ([523feaa](https://github.com/algolia/instantsearch-android/commit/523feaa))
* **release:** Add extra space in the fastfile changelog ([fc8d48b](https://github.com/algolia/instantsearch-android/commit/fc8d48b))
* **searcher:** Fix naming for index.getName() ([a020631](https://github.com/algolia/instantsearch-android/commit/a020631))
### Features
* **custombackend:** Change the Searcher to use a searchable index instead of concrete implementation ([#73](https://github.com/algolia/instantsearch-android/issues/73)) ([b070ad1](https://github.com/algolia/instantsearch-android/commit/b070ad1))

## 1.6.3 [Changes](https://github.com/algolia/instantsearch-android/compare/1.6.2...1.6.3) (2018-03-23)

### Bug Fixes
* **attrs.xml:** Expose prefix/suffix attributes ([f351758](https://github.com/algolia/instantsearch-android/commit/f351758))

## 1.6.2 [Changes](https://github.com/algolia/instantsearch-android/compare/1.6.1...1.6.2) (2018-03-16)

### Features
* **fastlane:** Build client at a specific commit if specified ([e1321ab](https://github.com/algolia/instantsearch-android/commit/e1321ab))

## 1.6.1 [Changes](https://github.com/algolia/instantsearch-android/compare/1.6.0...1.6.1) (2018-03-06)
> Shallow release to fix missing javadoc jar

## 1.6.0 [Changes](https://github.com/algolia/instantsearch-android/compare/1.5.0...1.6.0) (2018-03-06)

### Features

* **Searcher:** Unregister listeners ([886658f](https://github.com/algolia/instantsearch-android/commit/886658f))


## 1.5.0 [Changes](https://github.com/algolia/instantsearch-android/compare/1.4.1...v1.4.0) (2018-02-14)
This release adds **Multi-index Search** capabilities. See the associated [movies example](https://community.algolia.com/instantsearch-android/examples.html#movies-application).

### Bug fixes

* **BindingHelper:** Correct use of `DEFAULT_COLOR` ([1fa0b68](https://github.com/algolia/instantsearch-android/commit/1fa0b68))
* **BindingHelper:** databound color becomes ColorRes ([f16892b](https://github.com/algolia/instantsearch-android/commit/f16892b))
* **Hits:** Handle null variant as well ([a781c33](https://github.com/algolia/instantsearch-android/commit/a781c33))
* **Hits:** Ignore any view with null attribute ([611418b](https://github.com/algolia/instantsearch-android/commit/611418b))
* **RefinementList:** remove duplicates from merge ([2acd1cc](https://github.com/algolia/instantsearch-android/commit/2acd1cc))

### Features

* **BindingHelper:** Prefix/Suffix databinding attributes ([e4aacb0](https://github.com/algolia/instantsearch-android/commit/e4aacb0))
* **InstantSearch:** register widgets from fragment ([4a84999](https://github.com/algolia/instantsearch-android/commit/4a84999))
* **LayoutViews:** Implement findAny ([7768e9c](https://github.com/algolia/instantsearch-android/commit/7768e9c))
* **Searcher:** Multiple searchers identified by indexName/variant ([554013d](https://github.com/algolia/instantsearch-android/commit/554013d))


## 1.4.2 [Changes](https://github.com/algolia/instantsearch-android/compare/1.4.1...v1.4.0) (2018-02-14)

### Bug fixes

* **RefinementList**: Iterate until min(limit, values.size)

## 1.4.1 [Changes](https://github.com/algolia/instantsearch-android/compare/1.4.0...1.4.1) (2018-01-17)

### Bug Fixes

* **BindingAdapter:** Don't highlight when highlighted=false ([7c7467d](https://github.com/algolia/instantsearch-android/commit/7c7467d))
* **SearchBox:** Don't require NonNull attrs ([8bcfd25](https://github.com/algolia/instantsearch-android/commit/8bcfd25))
* **Searcher:** Only add LibraryVersion once to any client ([3efa187](https://github.com/algolia/instantsearch-android/commit/3efa187))
* **Searcher:** Restore missing import ([3acbe49](https://github.com/algolia/instantsearch-android/commit/3acbe49))
* **Searcher:** searchOnEmptyString was inverted ([6c55602](https://github.com/algolia/instantsearch-android/commit/6c55602))


## 1.4.0 [Changes](https://github.com/algolia/instantsearch-android/compare/1.3.0...1.4.0) (2017-12-13)

### Bug Fixes

* **Searcher.clearFacetRefinements:** Don't reset disjunctiveness of facets ([e654523](https://github.com/algolia/instantsearch-android/commit/e654523))
* **RefinementList:** Also check for convertView's class ([cdf8f15](https://github.com/algolia/instantsearch-android/commit/cdf8f15))
* **RefinementList:** Fix sorting of disjunctive refinements ([bfcd3d1](https://github.com/algolia/instantsearch-android/commit/bfcd3d1))
* **RefinementList:** Retain active facets on configuration change ([864c1bb](https://github.com/algolia/instantsearch-android/commit/864c1bb))
* **SearchResults:** Store disjunctiveFacets as map, fixes #54 ([3648a11](https://github.com/algolia/instantsearch-android/commit/3648a11)), closes [#54](https://github.com/algolia/instantsearch-android/issues/54)


### Features

* **FacetRefinementEvent:** Add disjunctiveness ([2ded719](https://github.com/algolia/instantsearch-android/commit/2ded719))
* **Searcher:** Implement getFacetRefinements ([8711c1c](https://github.com/algolia/instantsearch-android/commit/8711c1c))


## 1.3.0 [Changes](https://github.com/algolia/instantsearch-android/compare/1.2.3...1.3.0) (2017-11-29)
This release adds **Voice Search** capabilities to the SearchBox. See the associated [guide](https://community.algolia.com/instantsearch-android/widgets.html#voice-search).

### Bug Fixes

* **Searcher:** Send single event on search from intent ([64db885](https://github.com/algolia/instantsearch-android/commit/64db885))

### Features

* **Searcher:** search with intent (e.g. voice search) (#52) ([b17578b](https://github.com/algolia/instantsearch-android/commit/b17578b))


## 1.2.3 [Changes](https://github.com/algolia/instantsearch-android/compare/1.2.1...1.2.3) (2017-11-24)

### Bug Fixes

* **Hits:** Use scaleType if set ([6d4c92b](https://github.com/algolia/instantsearch-android/commit/6d4c92b))
* **scripts.serve:** update before serving ([8830d13](https://github.com/algolia/instantsearch-android/commit/8830d13))


## 1.2.1 [Changes](https://github.com/algolia/instantsearch-android/compare/1.2.0...1.2.1) (2017-11-14)

### Improvements

* **Glide**: Update to 4.3.1 ([94748717](https://github.com/algolia/instantsearch-android/commit/94748717))

## 1.2.0 [Changes](https://github.com/algolia/instantsearch-android/compare/1.1.4...1.2.0) (2017-11-13)

### Features

* **SearchBox/Searcher:** Apply query text changes to any other SearchBox ([8b5d3eb](https://github.com/algolia/instantsearch-android/commit/8b5d3eb))
> This enables SearchBoxes to display query text from other sources, such as the voice search dialog.


## 1.1.4 (2017-11-09)
> New release to fix missing artifact in the 1.1.3 release.

## 1.1.3 [Changes](https://github.com/algolia/instantsearch-android/compare/1.1.2...1.1.3) (2017-11-09)
### Bug Fixes
* **memoryleak:** Fix the memory leak ([3a65982](https://github.com/algolia/instantsearch-android/commit/3a65982))

### Improvements
* **gradle**: Update plugin to 3.0.0


## 1.1.2 [Changes](https://github.com/algolia/instantsearch-android/compare/1.1.1...1.1.2) (2017-10-04)

### Bug Fixes
* **Searcher:** refactor prepareWidget, fixing ResourceNotFound error ([90e3307](https://github.com/algolia/instantsearch-android/commit/de1b5fd7))


## 1.1.1 [Changes](https://github.com/algolia/instantsearch-android/compare/1.1.0...1.1.1) (2017-09-22)

### Bug Fixes
* **progressController:** Only setup if searchView registered ([90e3307](https://github.com/algolia/instantsearch-android/commit/90e3307))


## 1.1.0 [Changes](https://github.com/algolia/instantsearch-android/compare/1.0.2...1.1.0) (2017-08-30)

### New features
* **Searcher:** Forward from backend ([e9bbb8b](https://github.com/algolia/instantsearch-android/commit/e9bbb8b))


## 1.0.2 [Changes](https://github.com/algolia/instantsearch-android/compare/1.0.1...1.0.2) (2017-08-18)

### Bug Fixes
* **AlgoliaResultsListener:** Fix typo in class name


## 1.0.1 [Changes](https://github.com/algolia/instantsearch-android/compare/1.0.0...1.0.1) (2017-08-16)

### Bug Fixes
* **InstantSearch:** Restore constructor for single widget ([96eb97c](https://github.com/algolia/instantsearch-android/commit/96eb97c))



## 1.0.0 [Changes](https://github.com/algolia/instantsearch-android/compare/0.8.0...1.0.0) (2017-08-11)

### Bug Fixes

* **AlgoliaHitView:** Make result final ([7540f8c](https://github.com/algolia/instantsearch-android/commit/7540f8c))
* **docs:** Typo ([d30216b](https://github.com/algolia/instantsearch-android/commit/d30216b))
* **docs:** typo in _hero.scss ([289cc11](https://github.com/algolia/instantsearch-android/commit/289cc11))
* **InstantSearch:** Process listeners when registering AlgoliaFilters ([29eac00](https://github.com/algolia/instantsearch-android/commit/29eac00))
* **InstantSearchHelper:** Traverse as ViewGroup rather than View ([44caccb](https://github.com/algolia/instantsearch-android/commit/44caccb))
* **RefinementList:** Correct attribute obtainment ([a01e1e5](https://github.com/algolia/instantsearch-android/commit/a01e1e5))
* **Searcher:** Don't remove directly a facet that was requested twice ([5b9a7dd](https://github.com/algolia/instantsearch-android/commit/5b9a7dd))
* **TwoValuesToggle:** refine on first value before first toggle ([9562f1b](https://github.com/algolia/instantsearch-android/commit/9562f1b))


### Features

* **AlgoliaWidget:** Replace onReset method by subscription to ResetEvents ([ab09f84](https://github.com/algolia/instantsearch-android/commit/ab09f84))
* **events:** Send events on QueryText{Change,Submit} ([75b8b6b](https://github.com/algolia/instantsearch-android/commit/75b8b6b))
* **Events:** NumericRefinementEvents, enabling NumericSelector syncing ([ec119c2](https://github.com/algolia/instantsearch-android/commit/ec119c2))
* **FacetRefinementEvent:** Implement and leverage in OneValueToggle ([444f90c](https://github.com/algolia/instantsearch-android/commit/444f90c))
* **RefinementList:** Leverage FacetRefinementEvent ([669f101](https://github.com/algolia/instantsearch-android/commit/669f101))
* **ResultEvent:** expose response as typed SearchResults ([6e62fb8](https://github.com/algolia/instantsearch-android/commit/6e62fb8))
* **scripts:** script for javadoc update ([aafc643](https://github.com/algolia/instantsearch-android/commit/aafc643))
* **Stats:** autoHide ([be4dcd8](https://github.com/algolia/instantsearch-android/commit/be4dcd8))
* **Stats:** Use AppCompatTextView ([7a36475](https://github.com/algolia/instantsearch-android/commit/7a36475))
* **TwoValuesToggle:** Leverage FacetRefinementEvent ([81a7810](https://github.com/algolia/instantsearch-android/commit/81a7810))

## 0.8.0 (2017-03-27)

### Migration notice
* Rename InstantSearchHelper to InstantSearch ([f8d31b5](https://github.com/algolia/instantsearch-android/commit/f8d31b5))

### Features

* **Hits:** Keyboard Auto-hiding feature ([40815c0](https://github.com/algolia/instantsearch-android/commit/40815c0))
* **NumericSelector:** Most properties can be updated dynamically ([387d453](https://github.com/algolia/instantsearch-android/commit/387d453))

### Bug Fixes

* **NumericRefinement:** correct error message for checkOperatorIsValid ([8b40e3d](https://github.com/algolia/instantsearch-android/commit/8b40e3d))



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

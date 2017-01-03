---
title: Getting started
layout: main.pug
name: getting-started
category: intro
withHeadings: true
navWeight: 100
---

*This guide will walk you through the few steps needed to start a project with InstantSearch Android.*

## Searcher

- Construct a `Searcher` with your Application ID, the appropriate API Key and the name of the index you want to target:
```java
Searcher searcher = new Searcher(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);
```

  *You can also instantiate a Searcher by directly constructing it with an Index instance. This will let you use this index to  enable all features exposed in the [standard API client](https://github.com/algolia/algoliasearch-client-android), like [enabling the search cache](https://github.com/algolia/algoliasearch-client-android#search-cache) or [setting specific settings](https://github.com/algolia/algoliasearch-client-android#settings).*

## InstantSearchHelper

### With a single Widget

Construct an `InstantSearchHelper` with a `Searcher` instance and the `AlgoliaWidget` which will receive incoming results, then link it to your `SearchView` to receive its queries:

```java
AlgoliaWidget widget = (AlgoliaWidget) findViewById(R.id.myWidget);
InstantSearchHelper helper = new InstantSearchHelper(searcher, widget); // link the widget to the Searcher
searchView = (SearchView) findViewById(R.id.searchView);
instantSearchHelper.registerSearchView(this, searchView); // link the SearchView to the Searcher
```

### With several Widgets in the same Activity

If you have several AlgoliaWidgets in your Activity, you should create the `InstantSearchHelper` by directly providing a reference to this activity:

```java
InstantSearchHelper helper = new InstantSearchHelper(this, searcher); // link the widgets to the Searcher
searchView = (SearchView) findViewById(R.id.searchView);
```

If the activity contains a `SearchView` or our `SearchBox` widget, we will register it for you automatically.

----

You now have the basis of an InstantSearch application: an activity with a `SearchView` and one or several `AlgoliaWidget`, where changes in the SearchView's text trigger searches on your Algolia index which results are displayed in your widget. *Voilà !* 🎉

---
title: Main Concepts
layout: main.pug
name: concepts
category: main
withHeadings: true
navWeight: 10
---

**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Android API Client](https://github.com/algolia/algoliasearch-client-android) to provide you a high-level solution to quickly build various search interfaces.


In this guide, you will learn the key concepts of InstantSearch Android.


## Searcher

The main component of InstantSearch Android is the **Searcher**, which will wrap an [Algolia API `Client`](https://github.com/algolia/algoliasearch-client-android/blob/master/algoliasearch/src/main/java/com/algolia/search/saas/Client.java) and provide a level of abstraction over it.

The Searcher is responsible of all search requests: when `Searcher#search()` is called, the Searcher will fire a request with the current query, and will forward the search results to its **listeners**.

<img src="assets/img/diagram_searcher.png" align="center" height="400px"/>

### Listeners
A listener is an object implementing one or more of the following interfaces, that will be called after each search request:

- [`AlgoliaResultsListener`][algoliaresultslistener] to process new search results with `onResults`
- [`AlgoliaErrorListener`][algoliaerrorlistener] to handle search errors with `onError`

You can add a listener to a Searcher by calling `Searcher#register{Results,Error}Listener`.

_To avoid leaking memory, you should call `Searcher#destroy()` in your Activity/Fragment's [`onDestroy`](https://developer.android.com/guide/components/activities/activity-lifecycle.html#ondestroy) method to release existing listeners._

<img src="assets/img/diagram_listeners.png" align="center" />

## InstantSearch

The Searcher is UI-agnostic, and only communicates with its listeners. On top of it, we provide you a component which will link it to your user interface: **InstantSearch**.

InstantSearch will use the Searcher to react to changes in your application's interface, like when your user types a new query or interacts with Widgets.

Linked to a `SearchView`, it will watch its content to send any new query to the `Searcher`. When the query's results arrive, `InstantSearch` will forward them to its widgets.
<img src="assets/img/diagram_instantsearch.png" align="center" height="500px"/>

## Widgets

Widgets are the UI building blocks of InstantSearch Android, linked together by an `InstantSearch` to help you build instant-search interfaces. We provide some universal widgets such as the **`SearchBox`**, the **`Hits`** or the **`RefinementList`**, and you can easily create new ones by implementing one or several of the *widget interfaces*:

- [`AlgoliaResultsListener`][algoliaresultslistener] will let your widget react to new search results.
- [`AlgoliaErrorListener`][algoliaerrorlistener] will let your widget handle search errors.
- [`AlgoliaSearcherListener`][algoliasearcherlistener] will give your widget a reference to the `Searcher` to let it interact with the state of the search interface: [add new facets][addfacet], [refine on a value][addfacetrefinement], [know if there are more results to load][hasmorehits], ...

## Events

InstantSearch comes with an event system that lets you react during the lifecycle of a search query:
- when a **query is fired** via a `SearchEvent(Query query, int requestSeqNumber)`
- when its **results arrive** via a `ResultEvent(JSONObject content, Query query, int requestSeqNumber)`
- when a **query is cancelled** via a `CancelEvent(Request request, Integer requestSeqNumber)`
- when a **request errors** via a `ErrorEvent(AlgoliaException error, Query query, int requestSeqNumber)`
- when a new **facet refinement** is applied via a `FacetRefinementEvent(Operation operation, NumericRefinement refinement)`
- when a new **numeric refinement** is applied via a `NumericRefinementEvent(Operation operation, String attribute, String value)`

We use EventBus to dispatch events. You can register an object to the event bus using `EventBus.getDefault().register(this);` after which it will receive events on methods annotated by `@Subscribe`:

```java
public class Logger {
    Logger() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    onSearchEvent(SearchEvent e) {
        Log.d("Logger", "Search:" + e.query);
    }

    @Subscribe
    onResultEvent(ResultEvent e) {
        Log.d("Logger", "Result:" + e.query);
    }
}
```

[algoliaresultslistener]: javadoc/com/algolia/instantsearch/model/AlgoliaResultsListener.html
[algoliaerrorlistener]: javadoc/com/algolia/instantsearch/model/AlgoliaErrorListener.html
[algoliasearcherlistener]: javadoc/com/algolia/instantsearch/model/AlgoliaErrorListener.html

[addfacet]: javadoc/com/algolia/instantsearch/helpers/Searcher.html#addFacet-java.lang.String...-
[addfacetrefinement]: javadoc/com/algolia/instantsearch/helpers/Searcher.html#addFacetRefinement-java.lang.String-java.lang.String-
[hasmorehits]: javadoc/com/algolia/instantsearch/helpers/Searcher.html#hasMoreHits--

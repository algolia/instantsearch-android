---
title: Main Concepts
layout: main.pug
name: concepts
category: intro
---

**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Android API Client](https://github.com/algolia/algoliasearch-client-android) to provide you a high-level solution to quickly build various search interfaces.


In this guide, you will learn the key concepts of InstantSearch Android.


# Searcher

The main component of InstantSearch Android is the **Searcher**, which will wrap an [Algolia API `Client`](https://github.com/algolia/algoliasearch-client-android/blob/master/algoliasearch/src/main/java/com/algolia/search/saas/Client.java) and provide a level of abstraction over it.

The Searcher is responsible of all search requests: when `Searcher#search()` is called, the Searcher will fire a request with the current query, and will forward the search results to its **listeners**.

```
                                    ┌-> A implements AlgoliaResultsListener
Searcher.search(query) -> algolia --┤
                                    └-> B implements AlgoliaResultsListener
```


## Listeners
A listener is an object implementing the [`AlgoliaResultsListener`](instantsearch/src/main/java/com/algolia/instantsearch/model/AlgoliaResultsListener.java) interface: this object's `onResults` or `onError` method will be called after each search request returns to let you either process the results or handle the error. You can add a listener to a Searcher by calling `Searcher#registerListener()`.


```
                ┌→ (error) onError(final Query query, final AlgoliaException error);
new Search -> -─┤
                └→ (success) onResults(SearchResults results, boolean isLoadingMore);
```

# InstantSearchHelper

The Searcher is UI-agnostic, and only communicates with its listeners. On top of it, we provide you a component which will link it to your user interface: the **InstantSearchHelper**.

The InstantSearchHelper will use the Searcher to react to changes in your application's interface, like when your user types a new query or interacts with Widgets.

Linked to a `SearchView`, it will watch its content to send any new query to the `Searcher`. When the query's results arrive, the `InstantSearchHelper` will forward them to its `AlgoliaWidgets`.


```
        SearchView.onQueryTextListener.onQueryTextChange()
                               │
                               ↓
             searcher.search(searchView.getQuery())
                               │
            ┌──────────────────┴──────────────────┐
            ↓                                     ↓
Widget1.onResults(hits, isLoadingMore) Widget2.onResults(hits, isLoadingMore)
```

# Widgets

Widgets are the UI building blocks of InstantSearch Android, linked together by an `InstantSearchHelper` to help you build instant-search interfaces. We provide some basic widgets such as the **`SearchBox`**, the **`Hits`** or the **`RefinementList`**, and you can easily implement new ones by implementing the [`AlgoliaWidget`](instantsearch/src/main/java/com/algolia/instantsearch/ui/views/AlgoliaWidget.java) interface.

## Anatomy of an `AlgoliaWidget`

An **`AlgoliaWidget`** is a specialization of the `AlgoliaResultsListener` interface used by the `Searcher` to notify its listeners of search results. Beyond reacting to search results with `onResults` and to errors in `onError`, an `AlgoliaWidget` exposes an `onReset` method which will be called when the interface is reset (which you can trigger via `InstantSearchHelper#reset()`).
When linked to a `Searcher`, the widget's `setSearcher` method will be called to provide it a reference to its Searcher, which is useful to some widgets. For example, the `Hits` widget uses it to load more results as the user scrolls.

# Highlighting
<img src="docs/highlighting.png" align="right"/>

Visually highlighting the search result is [an essential feature of a great search interface](https://blog.algolia.com/inside-the-algolia-engine-part-5-highlighting-a-cornerstone-to-search-ux/). It will help your users understand your results by explaining them why a result is relevant to their query.

## With the `Hits` widget and system Views
A highlighting mechanism is built-in with the `Hits` widget. To highlight a textual attribute, simply add the `highlighted` attribute on its view:

```xml
<TextView
    android:id="@+id/name"
    algolia:attribute='@{"city"}'
    algolia:highlighted="@{true}"/>
```
This will highlight the attribute according to the query term, like you can see in the screenshot. The color used for highlighting is `R.color.colorHighlighting`, which you can override in your application.

Alternatively, you can specify `algolia:highlightingColor='@{"color/appDefinedColor"}'` to use a specific color for the current view.

Note that highlighting **only works automatically on TextViews**. if you implement a [custom hit view](#custom-hit-views), you should use the `Highlighter` to render the appropriate highlight, as explained in the next section.

## With custom Hit Views or a custom Widget

To highlight an attribute in your `AlgoliaHitView` or to highlight results received by your [custom widget](#anatomy-of-an-algoliawidget), you should use the **Highligter**.
This tool will let you build a highlighted [`Spannable`](https://developer.android.com/reference/android/text/Spannable.html) from a search result and an optional highlight color:

```java
final Spannable highlightedAttribute = Highlighter.getDefault().renderHighlightColor(result, attributeToHighlight, context);
```

The default Highlighter will highlight anything between `<em>` and `</em>`. You can configure the Highlighter to highlight between any pair of terms with `Highlighter.setDefault(newPrefix, newSuffix)`, or use a RegExp pattern to highlight any captured part with `Highlighter.setDefault(newPattern)`.

*See for example the [e-commerce app][ecommerce-url]'s [`CategoryOrTypeView`](https://github.com/algolia/instantsearch-android-examples/blob/master/ecommerce/src/main/java/com/algolia/instantsearch/examples/ecommerce/views/CategoryOrTypeView.java), a TextView which takes either the `category` or the `type` attribute of a record and [highlights it](https://github.com/algolia/instantsearch-android-examples/blob/master/ecommerce/src/main/java/com/algolia/instantsearch/examples/ecommerce/views/CategoryOrTypeView.java#L25) before displaying.*

# Progress indicator
<img src="docs/progress.gif" align="right" />

A useful pattern to improve your user's experience consists in displaying a progress indicator when there are ongoing requests still waiting to complete.

By default, the InstantSearchHelper will display an indeterminate [`ProgressBar`](https://developer.android.com/reference/android/widget/ProgressBar.html) in your `SearchView` as long as some requests are still incomplete. This loader is shown using animations when the target device is recent enough (>= API 14), or after a small delay to avoid blinking.
You can change this delay by calling `InstantSearchHelper#enableProgressBar(int)` with a delay in milliseconds, or disable this progress indicator with `InstantSearchHelper#disableProgressBar()`.

Alternatively, you can implement your own progress logic by using a [`SearchProgressController`](https://github.com/algolia/instantsearch-android/blob/master/instantsearch/src/main/java/com/algolia/instantsearch/SearchProgressController.java).  
Once instantiated, a **SearchProgressController** will inform its [`ProgressListener`](https://github.com/algolia/instantsearch-android/blob/master/instantsearch/src/main/java/com/algolia/instantsearch/SearchProgressController.java#L99) when some requests are sent with `onStart()`, and will call `onStop()` when all current requests have returned.

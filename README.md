# InstantSearch Android
**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Android API Client](https://github.com/algolia/algoliasearch-client-android) to provide you a high-level solution to quickly build various search interfaces.

# Demo
You can see InstantSearch Android in action in our Examples repository, in which we published two example apps built with InstantSearch:

| [Media app](media-url) | [E-commerce app](ecommerce-url) |
| ------------- | ------------- |
| [![animated screenshot of media app][media-gif]][media-url] | [![animated screenshot of ecommerce app][ecommerce-gif]][ecommerce-url] |


# Getting started

This guide will walk you through the few steps needed to start a project with InstantSearch Android, while explaining its basic concepts.

## Searcher
The main component of InstantSearch Android is the **Searcher**, which will wrap an [Algolia Client](https://github.com/algolia/algoliasearch-client-android/blob/master/algoliasearch/src/main/java/com/algolia/search/saas/Client.java) and provide a level of abstraction over it.

To create a Searcher, simply call its constructor with your Application ID, the appropriate API Key and the name of the index you want to target:
```java
new Searcher(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);
```

*You can also instantiate a Searcher by directly constructing it with an Index instance. This will let you use this index to  enable all features exposed in the [standard API client](https://github.com/algolia/algoliasearch-client-android), like [enabling the search cache](https://github.com/algolia/algoliasearch-client-android#search-cache) or [setting specific settings](https://github.com/algolia/algoliasearch-client-android#settings).*

Once created, the Searcher is responsible of all search requests: when `Searcher#search()` is called, the Searcher will fire a request with the current query, and will forward the search results to its **listeners**.  
You can add a listener to a Searcher by calling `Searcher#registerListener()` with an object implementing the `AlgoliaResultsListener` interface. This object's `onResults` or `onError` method will be called after each search request returns to let you either process the results or handle the  error.

## InstantSearchHelper

The Searcher is UI-agnostic, and only communicates with its listeners. On top of it, we provide you a component which will link it to your user interface: the **InstantSearchHelper**.

The InstantSearchHelper will use the Searcher to react to changes in your application's interface, like when your user types a new query or interacts with Widgets. You can either use it with a single widget which will receive incoming results, or with several that will interact together in the same activity.

### With a single Widget

When you have only one widget, the `InstantSearchHelper` will simply send any incoming results or error to this widget.  
Simply call its constructor with a `Searcher` instance and the `AlgoliaWidget` which will receive incoming results:

```java
Searcher searcher = /* initialize searcher as explained earlier */;
AlgoliaWidget widget = (AlgoliaWidget) findViewById(R.id.myWidget);
InstantSearchHelper helper = new InstantSearchHelper(searcher, widget); // link the widget to the searcher
```

Your widget is now linked to the `Searcher` and will receive search results when a new search is fired, for example by calling `searcher.search(queryTerms)`. However, this is not enough: it is very likely that you want to trigger search requests when your user types a query in your `SearchView`. To do so, simply register it on the `InstantSearchHelper` in your search Activity:

```java
searchView = (SearchView) findViewById(R.id.searchView);
instantSearchHelper.registerSearchView(this, searchView);
```

Once registered, any change to the SearchView will trigger a search query with the new text, and results will be propagated to your widget.

### With several Widgets in the same Activity

If you have several AlgoliaWidgets in your Activity, you should create the InstantSearchHelper by directly providing a reference to this activity:

```java
InstantSearchHelper helper = new InstantSearchHelper(this, searcher);
```

When instantiating the InstantSearchHelper with an Activity, we will scan its layout to pick-up every AlgoliaWidget it contains.
If the activity contains a `SearchView` or our `SearchBox` widget, we will register it for you as explained above.

----
After following those steps, you have the basis of an InstantSearch application: an activity with a `SearchView` and one or several `AlgoliaWidget`, where changes in the SearchView's text trigger searches on your Algolia index which results are displayed in your widget. 🎉

## Widgets

Widgets are the UI building blocks of InstantSearch Android, linked together by an `InstantSearchHelper` to help you build instant-search interfaces. We provide some basic widgets such as the **`SearchBox`**, the **`Hits`** or the **`RefinementList`**, and you can easily implement new ones by implementing the `AlgoliaWidget` interface.

### Anatomy of an `AlgoliaWidget`

An `AlgoliaWidget` is a specialization of the `AlgoliaListener` interface used by the `Searcher` to notify its listeners of search results. Beyond reacting to search results with `onResults` and to errors in `onError`, an `AlgoliaWidget` exposes an `onReset` method which will be called when the interface is resetted (which you can trigger via `InstantSearchHelper#reset()`).  
When linked to a `Searcher`, the widget's `setSearcher` method will be called to provide it a reference to its Searcher, which is useful to some widgets. For example, the `Hits` widget uses it to load more results as the user scrolls.

### SearchBox

The SearchBox is a specialized `SearchView` which provides some customization options and facility methods. Apart from the existing `SearchView` attributes, it exposes two attributes you can specify in its XML definition:

```xml
<com.algolia.instantsearch.views.SearchBox
    android:id="@+id/searchBox"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:queryHint="Search"
    algolia:autofocus="true"
    algolia:submitButtonEnabled="false" />
```

- The **autofocus** attribute, when `true`, will make the SearchBox request the user's focus when displayed. (defaults to `false`)
- The **submitButtonEnabled** attribute, when `true`, will display the SearchBox with its submit button. This button is hidden by default: as every keystroke will update the results, it is usually misleading to display a submit button.

[media-gif]: ./docs/media.gif
[ecommerce-gif]: ./docs/ecommerce.gif
[media-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/media
[ecommerce-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/ecommerce

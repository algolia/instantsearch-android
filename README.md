# InstantSearch Android
**InstantSearch Android** is a library providing widgets and helpers to help you build the best instant-search experience on Android with Algolia.
It is built on top of Algolia's [Android API Client](https://github.com/algolia/algoliasearch-client-android) to provide you a high-level solution to quickly build various search interfaces.

# Demo
You can see InstantSearch Android in action in our Examples repository, in which we developed two example apps built with InstantSearch:

## Media
[![animated screenshot of media app][media-gif]][media-url]

## Ecommerce
[![animated screenshot of ecommerce app][ecommerce-gif]][ecommerce-url]

# Getting started

This guide will walk you through the few steps needed to start a project with InstantSearch Android, while explaining its basic concepts.

## Searcher
The main component of InstantSearch Android is the **Searcher**, which will wrap an [Algolia Client](https://github.com/algolia/algoliasearch-client-android/blob/master/algoliasearch/src/main/java/com/algolia/search/saas/Client.java) and provide a level of abstraction over it.

To create a Searcher, simply call its constructor with your Application ID, the appropriate API Key and the name of the index you want to target:
```java
new Searcher(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);
```

You can also instantiate a Searcher by directly constructing it with an Index instance. This will let you use this index to enable all features exposed in the [standard API client](https://github.com/algolia/algoliasearch-client-android), like [enabling the search cache](https://github.com/algolia/algoliasearch-client-android#search-cache) or [setting specific settings](https://github.com/algolia/algoliasearch-client-android#settings).  

Once created, the Searcher is responsible of all search requests: when `Searcher#search()` is called, the Searcher will fire a request with the current query, and will forward the search results to its **listeners**.  
You can add a listener to a Searcher by calling `Searcher#registerListener()` with an object implementing the `AlgoliaResultsListener` interface. This object's `onResults` or `onError` method will be called after each search request returns to let you either process the results or handle the  error.

## InstantSearchHelper

The Searcher is UI-agnostic, and only communicates with its listeners. On top of it, we provide you a component which will link it to your user interface: the **InstantSearchHelper**.

The InstantSearchHelper will use the Searcher to react to changes in your application's interface, like when your user types a new query or interacts with Widgets. To create it, you need to provide a Searcher instance and either an AlgoliaWidget which will receive incoming results:

```java
Searcher searcher = /* initialize searcher as explained earlier */;
AlgoliaWidget widget = (AlgoliaWidget) findViewById(R.id.myWidget);
InstantSearchHelper helper = new InstantSearchHelper(searcher, widget); // link the widget to the searcher
```

Your widget is now linked to the `Searcher` and will receive search results when a new search is fired, for example by calling `searcher.search(queryTerms)`. However, this is not enough: it is very likely that you want to trigger search requests when your user types a query in your SearchView. To do so, simply register it on the `InstantSearchHelper` in your search Activity:

```java
searchView = (SearchView) findViewById(R.id.searchView);
instantSearchHelper.registerSearchView(this, searchView);
```

Once registered, any change to the SearchView will trigger a search query with the new text, and results will be propagated to your widget.

If you have several AlgoliaWidgets in your Activity, you should create the InstantSearchHelper by directly providing a reference to this activity:

```java
InstantSearchHelper helper = new InstantSearchHelper(this, searcher);
```

When instantiating the InstantSearchHelper, we will scan your activity to pick-up every AlgoliaWidget in its layout.
If the activity contains a `SearchView` or our `SearchBox` widget, we will automatically register it as explained above.

----
After following those steps, you have the basis of an InstantSearch application: an activity with a SearchView and an AlgoliaWidget, where changes in the SearchView's text trigger searches on your Algolia index which results are displayed in your widget. **Congratulations!** 🎉

[media-gif]: ./docs/media.gif
[ecommerce-gif]: ./docs/ecommerce.gif
[media-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/media
[ecommerce-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/ecommerce

---
title: Multi Index
layout: main.pug
name: guide-multi-index
category: main
withHeadings: true
navWeight: 4
---

Sometimes a single search interface is not enough to fit your use case. Maybe
you want to display a **single search bar**, but search in **multiple indices** at the
same time. Maybe you just want to show a minimal search interface _on your home page_, and a _more detailed view_ in a second screen.

For all those use-cases where a single interface would not work, we provide a
feature: **Multi-Index**.

## Principles

A multi-index project consists in several search interfaces that are meant to
work independently. It consists in two or more sets of:
- a `Searcher`
- an `InstantSearch`
- a group of [Widgets](widgets.html)

### Variant

To associate the widgets to the Searcher, we provide a `variant` attribute that you can set on widgets:
```xml
<MyWidget
    android:id="@+id/myWidget"
    ...
    algolia:variant="products" />
```
_By default each Searcher will use its index name as a variant, but you can [choose your own](https://community.algolia.com/instantsearch-android/javadoc/com/algolia/instantsearch/helpers/Searcher.html#create-java.lang.String-java.lang.String-java.lang.String-java.lang.String-)._


## Usage

### One SearchBox, Multiple Indices
<img src="assets/img/movies.gif" class="img-object" align="right"/>

This is the typical multi index use case: you want to show results from several
sources in a single interface.

To do so, you need to instantiate two Searchers, link each to its widgets via an
InstantSearch, then connect them to the same SearchBox through a
`SearchBoxViewModel`:

<br /><br /><br /><br /><br /><br /><br />
```java
searcherMovies = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, "movies");
searcherActors = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, "actors");

// Create a SearchBoxViewModel with your SearchBox
searchBoxViewModel = new SearchBoxViewModel(this.findViewById(R.id.include_searchbox););
// Link it to your Activity's widgets
new InstantSearch(this, searcherMovies).registerSearchView(this, searchBoxViewModel);
new InstantSearch(this, searcherActors).registerSearchView(this, searchBoxViewModel);
```

Each `InstantSearch` helper will know which widgets it should handle thanks to its [**variant**](multi-index.html#variant).
You must set a variant on each widget to connect it to the right `Searcher`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- ... -->
    <com.algolia.instantsearch.ui.views.Hits
        android:id="@+id/hitsMovies"
        ...
        algolia:itemLayout="@layout/item_movie"
        algolia:variant="movies" />

    <com.algolia.instantsearch.ui.views.Hits
        android:id="@+id/hitsActors"
        ...
        algolia:itemLayout="@layout/item_actor"
        algolia:variant="actors" />
```

**You can check the [Movies app](https://github.com/algolia/instantsearch-android-examples/tree/master/movies)**, which demonstrates such a pattern both displayed [**as sections**](https://github.com/algolia/instantsearch-android-examples/blob/master/movies/src/main/java/com/algolia/instantsearch/examples/movies/MoviesSectionsActivity.java) and [**as tabs**](https://github.com/algolia/instantsearch-android-examples/blob/master/movies/src/main/java/com/algolia/instantsearch/examples/movies/MoviesTabsActivity.java).

### Several independant interfaces

This fits the more generic use-cases where you want to have several independent search interfaces in your app:

- Showcasing popular results in your app's homepage, then offering a full-fledged search interface
- Displaying two search interfaces alongside, each one searching a different dataset with different widgets
- ...

To do so, you will create two (or more) independent groups of widgets and helpers:

```java
searcherMain = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_MAIN);
searcherOther = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_OTHER);

// In your first interface's Activity, link the first searcher to its widgets:
new InstantSearch(this, searcherMain);

// Likewise in your second interface's Activity:
new InstantSearch(this, searcherOther);
```


If both groups of widgets are embedded in the same layout, you need to specify a [variant](multi-index.html#variant) on each widget to specify to which Searcher it belongs:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- ... -->
    <com.algolia.instantsearch.ui.views.SearchBox
        android:id="@+id/searchBoxMain"
        ...
        algolia:variant="main" />

    <com.algolia.instantsearch.ui.views.SearchBox
        android:id="@+id/searchBoxOther"
        ...
        algolia:variant="other" />

    <com.algolia.instantsearch.ui.views.Hits
        android:id="@+id/hitsMain"
        ...
        algolia:itemLayout="@layout/item_movie"
        algolia:variant="main" />

    <com.algolia.instantsearch.ui.views.Hits
        android:id="@+id/hitsOther"
        ...
        algolia:itemLayout="@layout/item_actor"
        algolia:variant="other" />
```

### Query suggestions
<img src="assets/img/query_suggestions.gif" class="img-object" align="right"/>

When your user interacts with a SearchBox, you can help them discover what they could search for by providing **Query suggestions**.

This is a specific kind of multi-index interface: your main search interface will be using a regular index,
while you will display suggestions as the user types from your [Query Suggestions index](https://www.algolia.com/doc/guides/analytics/query-suggestions/?language=php#getting-started-with-query-suggestions).

To display the suggestions in your Android app, follow these steps:

- Create a `ContentProvider` by subclassing `QuerySuggestionsContentProvider` to
  provide [an `Index`](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/java/com/algolia/instantsearch/examples/querysuggestions/QSContentProvider.kt#L13) and [a limit](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/java/com/algolia/instantsearch/examples/querysuggestions/QSContentProvider.kt#L9)
- In your [searchable configuration](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/res/xml/searchable.xml), mention your implementation and the action
  `intent.action.SEARCH`:
  ```xml
  android:searchSuggestIntentAction="android.intent.action.SEARCH"
  android:searchSuggestAuthority="com.algolia.instantsearch.examples.querysuggestions.QSContentProvider"
  ```
- In your AndroidManifest.xml, declare this [`<provider>`](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/AndroidManifest.xml#L31) and the [`<intent-filter>` for the receiving activity](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/AndroidManifest.xml#L22)
- In that activity, [use new intents to search](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/java/com/algolia/instantsearch/examples/querysuggestions/QuerySuggestionsActivity.kt#L25)

- Optionally, customize the suggestion layout by specifying a [`searchViewStyle`](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/res/values/styles.xml#L9) with a custom [`suggestionRowLayout`](https://github.com/algolia/instantsearch-android-examples/blob/master/querysuggestions/src/main/res/layout/layout_suggestion.xml).
  Your custom layout, **must include a `TextView` with id `edit_query` and an `ImageView` with id `text1`.**

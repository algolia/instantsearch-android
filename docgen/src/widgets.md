---
title: Widgets
layout: main.pug
name: widgets
category: main
withHeadings: true
navWeight: 5
---

## SearchBox
<img src="assets/img/widget_SearchBox.png" class="img-object" align="right" />
<!-- TODO: Document SearchBox in menu, using SearchableConfiguration -->

The **SearchBox** is a specialized `SearchView` which provides some customization options and facility methods. It supports all existing `SearchView` attributes and two new ones that you can specify in its XML definition:

```xml
<com.algolia.instantsearch.views.SearchBox
    android:id="@+id/searchBox"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:queryHint="Search"
    algolia:autofocus="true"
    algolia:submitButtonEnabled="false" />
```

- **`autofocus`**, when `true`, will make the SearchBox request the user's focus when displayed. (defaults to `false`)
- **`submitButtonEnabled`**, when `true`, will display the SearchBox with its submit button. This button is hidden by default: as every keystroke will update the results, it is usually misleading to display a submit button.

### Progress indicator
<img src="assets/img/progress.gif" class="img-object" align="right" />
<!-- TODO: Move to separate guide -->


A useful pattern to improve your user's experience consists in displaying a progress indicator when there are ongoing requests still waiting to complete.

By default, InstantSearch will display an indeterminate [`ProgressBar`][docs-progressbar] in your `SearchView` as long as some requests are still incomplete. This loader is shown using animations when the target device is recent enough (>= API 14), or after a small delay to avoid blinking.
You can change this delay by calling `InstantSearch#enableProgressBar(int)` with a delay in milliseconds, or disable this progress indicator with `InstantSearchHelper#disableProgressBar()`.

Alternatively, you can implement your own progress logic by using a [`SearchProgressController`](https://github.com/algolia/instantsearch-android/blob/master/instantsearch/src/main/java/com/algolia/instantsearch/events/SearchProgressController.java)
Once instantiated, a **SearchProgressController** will inform its [`ProgressListener`](https://github.com/algolia/instantsearch-android/blob/master/instantsearch/src/main/java/com/algolia/instantsearch/events/SearchProgressController.java#L98) when some requests are sent with `onStart()`, and will call `onStop()` when all current requests have returned.

### Voice search
<img src="assets/img/voice.gif" class="img-object" align="right" />

Voice search is a great way for your users to express their queries with almost no friction. You can add it in a few minutes in your InstantSearch interface:

- [Create a Searchable Configuration](https://developer.android.com/guide/topics/search/search-dialog.html#SearchableConfiguration) (`searchable.xml`)
- Declare your InstantSearch activity as a [Searchable Activity](https://developer.android.com/guide/topics/search/search-dialog.html#DeclaringSearchableActivity)

_Performing a search_ is then handled by InstantSearch. You just need to use the eventual search intent in your Activity's `onCreate` (or `onCreateOptionsMenu` if your `SearchBox` is in a menu):
```java
searcher.search(getIntent()); // Show results for empty query (on app launch) / voice query (from intent)
```

*If your activity's [launchMode](https://developer.android.com/guide/topics/manifest/activity-element.html#lmode) is not `standard`, you need to handle the intent in `onNewIntent` as well:*
```java
@Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    searcher.search(intent); // Show results for voice query (from intent)
}
```

## Hits
<img src="assets/img/widget_Hits.png" class="img-object" align="right"/>

The **Hits** widget is made to display your search results in a flexible way. Built over a [`RecyclerView`][docs-recyclerview], it displays a limited window into a large data set of search results.

This widget exposes a few attributes that you can set in its xml definition:

```xml
<com.algolia.instantsearch.views.Hits
    android:id="@+id/hits"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    algolia:autoHideKeyboard="true"
    algolia:disableInfiniteScroll="false"
    algolia:remainingItemsBeforeLoading="10"
    algolia:itemLayout="@layout/hits_item">
```

- **`autoHideKeyboard`**, when `true`, closes the keyboard when the Hits are scrolled. (defaults to `false`)
- **`disableInfiniteScroll`**, when `true`, disables the [**Infinite scroll**][infinite-scroll] feature (defaults to `false`)
- **`remainingItemsBeforeLoading`** sets the minimum number of remaining hits to load the next page: if you set it to 10, the next page will be loaded when there are less than 10 items below the last visible item. (defaults to 5)
- **`itemLayout`**, finally, is used to determine the appearance of the search results.

This last attribute should reference a layout file in which you will [describe how a search result will be displayed][guide-layout]. When receiving results from its `Searcher`, this widget will bind the given layout to each result to display its attributes in the appropriate Views.

[infinite-scroll]: widgets.html#infinite-scroll
[guide-layout]: getting-started.html#itemlayout
### Data Binding

This binding is done using the [Android DataBinding Library][docs-databinding], which allows to link a layout to an application's data. To enable this feature, add `dataBinding.enabled true` to your app's `build.gradle` under `android`:
```groovy
android {
    dataBinding.enabled true
    //...
}
```

You can now create the hit layout. The layout file should use a `<layout></layout>` root node, followed by a regular ViewGroup (such as a `LinearLayout`). You can then describe what attributes should be mapped to each View as follows:

```xml
<layout
    xmlns:algolia="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <ImageView
            android:id="@+id/contactFace"
            style="@style/AppTheme.View.ContactImage"
            algolia:attribute='@{"img"}'/>

        <TextView
            android:id="@+id/contactCompany"
            style="@style/AppTheme.View.ContactText.Small"
            algolia:attribute='@{"company"}'/>

        <RatingBar
            android:id="@+id/contactScore"
            style="@style/AppTheme.View"
            algolia:attribute='@{"score"}'/>
    </RelativeLayout>
</layout>
```

For each `View` which should receive a result's attribute, you can specify `algolia:attribute='@{"foo"}'` to map the record's `foo` attribute to this View.

Currently, the Hits widget handles natively the following Views and their subclasses:


|View Class | Use of the attribute | Method called | Notes|
| --------- | -------------------- | ------------- | ---- |
|TextView   | text content | setText(attributeValue) | Can be highlighted
|EditText   | hint content | setHint(attributeValue) | Can be highlighted
|ImageView  | bitmap image URL | setBitmapImage(attributeBitmap)| Parsed to an URL, then loaded asynchronously
|RatingBar  | rating value| setRating(attributeValue)| Parsed as Float
|ProgressBar| progress value | setProgress(attributeValue)| Parsed as Float and rounded to the nearest Integer

### Custom hit views

Apart from these ones, any `View` can be used to hold an attribute if it implements the [`AlgoliaHitView`][docs-hitview] interface. In this case, we will call `onUpdateView(JSONObject result)` and the view will be responsible of using the result's JSON to display the hit.

*See for example the [media app][media-url]'s [`TimestampHitView`](https://github.com/algolia/instantsearch-android-examples/blob/master/media/src/main/java/com/algolia/instantsearch/examples/media/views/TimestampHitView.java), a TextView which transforms a timestamp attribute to display a human-readable date instead.*

### Infinite scroll

An infinite scroll mechanism is built-in to load more results as the user scrolls.
Enabled by default, it will watch the state of the Hits to load more results before the user reaches the end of the current page.

As explained [in the attributes description](widgets.html#hits), you can use the attributes `remainingItemsBeforeLoading` and `disableInfiniteScroll` to control or disable this feature.

### Empty View

The Hits widget implements an empty view mechanism to display an alternative View if there are no results to display, following the [AdapterView's interface][docs-adapterview].
If you add a View to your layout with the id **`@android:id/empty`**, it will be displayed instead of the Hits when there is no data to display.  You can also set it programmatically using `Hits#setEmptyView(View)`.

### Item Click Listener

As the Hits widget is based on a `RecyclerView`, we use [Hugo Visser's `ItemClickSupport`](http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/) to let you react to clicks on individual hits. To do so, add an [`OnItemClickListener`][docs-clicklistener] to your hits:

```java
hits.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
    @Override
    public void onItemClick(RecyclerView recyclerView, int position, View v) {
        JSONObject hit = hits.get(position);
        // Do something with the hit
    }
 });
```

### Prefixing and suffixing attributes
You might need to apply a prefix or suffix to a View's attribute value. A common use case is image attributes having only a relative path like `foo.png`, and requiring a prefix like `https://example.com/files/` to get the full image path.

To achieve this, you can add either or both `prefix` / `suffix` attributes to your view. For example, if your attribute value is `foo` but you want the full value to be `https://example.com/files/foo.png`:
```xml
<ImageView
    android:id="@+id/name"
    algolia:attribute='@{"image"}'
    algolia:prefix='@{"https://example.com/files/"}'
    algolia:suffix='@{".png"}'/>
```




### Highlighting
<img src="assets/img/highlighting.png" class="img-object" align="right"/>

Visually highlighting the search result is [an essential feature of a great search interface][explain-highlighting]. It will help your users understand your results by explaining them why a result is relevant to their query.

The `Hits` widget automatically handles highlighting. To highlight a textual attribute, simply add the `highlighted` attribute on its view:

```xml
<TextView
    android:id="@+id/name"
    algolia:attribute='@{"city"}'
    algolia:highlighted="@{true}"/>
```
This will highlight the attribute according to the query term, like you can see in the screenshot. The color used by default for highlighting is `R.color.colorHighlighting`, which you can override in your application.

You can also specify `algolia:highlightingColor='@{R.color.color_foo}'` on a `View` to use a specific color for this one only.


Note that highlighting **only works automatically on TextViews**. if you implement a [custom hit view](widgets.html#custom-hit-views) or to highlight results received by your [custom widget](widgets.html#anatomy-of-an-algoliawidget), you should use the [`Highlighter`](javadoc/com/algolia/instantsearch/helpers/Highlighter.html).
This tool will let you build a highlighted [`Spannable`](https://developer.android.com/reference/android/text/Spannable.html) from a search result and an optional highlight color:

```java
final Spannable highlightedAttribute = Highlighter.getDefault()
                                                  .setInput(result, attributeToHighlight)       // EITHER using Algolia's _highlightResult
                                                  .setInput("My <em> highlighted</em> string")  // OR using a raw markup string
                                                  .setColor(context)                  // EITHER using the default R.color.colorHighlighting
                                                  .setColor(Color.RED)                // OR using a system Color int
                                                  .setColor(R.color.myColor, context) // OR using a ColorRes
                                                  .render();
```

The default Highlighter will highlight anything between `<em>` and `</em>`. You can configure the Highlighter to highlight between any pair of terms with `Highlighter.setDefault(newPrefix, newSuffix)`, or use a RegExp pattern to highlight any captured part with `Highlighter.setDefault(newPattern)`.

*See for example the [e-commerce app][ecommerce-url]'s [`CategoryOrTypeView`](https://github.com/algolia/instantsearch-android-examples/blob/master/ecommerce/src/main/java/com/algolia/instantsearch/examples/ecommerce/views/CategoryOrTypeView.java), a TextView which takes either the `category` or the `type` attribute of a record and [highlights it](https://github.com/algolia/instantsearch-android-examples/blob/master/ecommerce/src/main/java/com/algolia/instantsearch/examples/ecommerce/views/CategoryOrTypeView.java#L25) before displaying.*

## NumericSelector
<img src="assets/img/widget_NumericSelector.png" class="img-object" align="right"/>

The **NumericSelector** is a filtering widget made to display some values for a [facet][guide-faceting] and let the user refine the search results by selecting one.

You can configure its behavior with several attributes:
<br /><br /><br /><br /> <!-- Line breaks to avoid code sample being squeezed by the floating image -->

```xml
<com.algolia.instantsearch.ui.views.filters.NumericSelector
        android:id="@+id/selector"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:spinnerMode="dialog"
        algolia:attribute="salePrice"
        algolia:labels="&lt; 10€, &lt; 100€, &lt; 1000€"
        algolia:defaultLabel="Any price"
        algolia:operator="lt"
        algolia:values="10, 100, 1000"
        algolia:autoHide="true"/>
```

- **`attribute`** defines which faceted attribute will be used by the widget.
- **`operator`** is the comparison operator used for filtering.
  This attribute accepts the following values:
  - `"lt"` for **lower than** (`<`)
  - `"le"` for **lower or equal** (`<=`)
  - `"eq"` for **equal** (`==`)
  - `"ne"` for **not equal** (`!=`)
  - `"ge"` for **greater or equal** (`>=`)
  - `"gt"` for **greater than** (`>`)
- **`labels`** should be a list of the labels to display for each choice item.
- **`values`** should be a list of the values to filter with for each choice item.
- **`defaultLabel`** is the label for the default option (the first option of the selector does not filter on any value to let the user cancel the filtering)
- **`autoHide`** if `true`, this widget will be hidden when there are no results.

**Note that if you display a `NumericSelector` inside a [`PopupWindow`][docs-popupwindow], you must set its `spinnerMode` to dialog with `android:spinnerMode="dialog"`. Failing to do so will result in a [`BadTokenException`](https://stackoverflow.com/a/18781297/3109189).**


## RefinementList
<img src="assets/img/widget_RefinementList.png" class="img-object" align="right"/>

The **RefinementList** is a filtering widget made to display your [facets][guide-faceting] and let the user refine the search results.

Four attributes allow you to configure how it will filter your results:
<br /><br /><br /><br /> <!-- Line breaks to avoid code sample being squeezed by the floating image -->

```xml
<com.algolia.instantsearch.views.RefinementList
            android:id="@+id/refinements"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            algolia:attribute="city"
            algolia:limit="10"
            algolia:operation="or"
            algolia:sortBy="['isRefined', 'count:desc']"/>
```

- **`attribute`** defines which faceted attribute will be used by the widget.
- **`operation`** can either be `"or"` or `"and"`, to control if the results should match *any* selected value or *all* selected values. (defaults to `"or"`)
- **`limit`** is the maximum amount of facet values we will display (defaults to 10). If there are more values, we will display those with the bigger counts.
- **`sortBy`** controls the sort order of the attributes. You can either specify a single value or an array of values to apply one after another.

  This attribute accepts the following values:
  - `"isRefined"` to sort the facets by displaying first currently refined facets
  - `"count:asc"` to sort the facets by increasing count
  - `"count:desc"` to sort the facets by decreasing count
  - `"name:asc"` to sort the facet values by alphabetical order
  - `"name:desc"` to sort the facet values by reverse alphabetical order

In the previous code sample, `sortBy="['isRefined', 'count']"` will display the refined facets before the non-refined ones, and will then sort them by decreasing count.

## Stats
<img src="assets/img/widget_Stats.png" class="img-object" align="right"/>

**Stats** is a widget for displaying statistics about the current search result. You can configure it with two attributes:

```xml
<com.algolia.instantsearch.views.Stats
            android:id="@+id/stats"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            algolia:resultTemplate="{nbHits} results found in {processingTimeMS} ms"
            algolia:errorTemplate="Error, please try again"
            algolia:autoHide="true"/>
```
- **`resultTemplate`** defines what this widget will display when a search request returns successfully. It takes the form of a templated string where we will replace the following templates:
  - `{nbHits}` will be replaced by the hit count for the current query
  - `{processingTimeMS}` will be replaced by the time the server took to process the request, in milliseconds
  - `{hitsPerPage}` will be replaced by the maximum number of hits returned per page
  - `{nbPages}` will be replaced by the number of pages corresponding to the number of hits
  - `{page}` will be replaced by the index of the current page (zero-based)
  - `{query}` will be replaced by the query text

  The default `resultTemplate` is `"{nbHits} results found in {processingTimeMS} ms"`.

- **`errorTemplate`** defines what this widget will display when a search query returns with an error. It takes the form of a templated string where we will replace the following templates:
  - `{error}` will be replaced by the error message
  - `{query}` will be replaced by the query text

- **`autoHide`** if `true`, this widget will be hidden when there are no results.

  If you don't specify an `errorTemplate`, the Stats widget will be hidden when a query returns an error.

## Custom widgets

If none of these widgets fits your use-case, you can implement your own!

Any `View` in your Activity implementing [one or more widget interfaces][interfaces] will be picked-up by `InstantSearch` at instantiation. Most widgets will implement two methods:
- [AlgoliaResultsListener](javadoc/com/algolia/instantsearch/model/AlgoliaResultsListener.html)'s `onResults` to be called when new results are received
- [AlgoliaErrorListener](javadoc/com/algolia/instantsearch/model/AlgoliaErrorListener.html)'s `onError` to be called when there is an error

You can also implement the [`AlgoliaSearcherListener`](javadoc/com/algolia/instantsearch/model/AlgoliaSearcherListener.html) interface to get a reference to the `Searcher` used in your search interface. It will enable your widget to uses the [Searcher's programmatic API][docs-searcher].

[media-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/media
[ecommerce-url]: https://github.com/algolia/instantsearch-android-examples/tree/master/ecommerce
[explain-highlighting]: https://www.algolia.com/doc/faq/searching/what-is-the-highlighting/
[docs-searcher]: javadoc/com/algolia/instantsearch/helpers/Searcher.html
[docs-hitview]: https://github.com/algolia/instantsearch-android/blob/master/instantsearch/src/main/java/com/algolia/instantsearch/ui/views/AlgoliaHitView.java
[guide-faceting]: https://www.algolia.com/doc/guides/searching/filtering/
[docs-progressbar]: https://developer.android.com/reference/android/widget/ProgressBar.html
[docs-recyclerview]: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
[docs-databinding]: https://developer.android.com/topic/libraries/data-binding/index.html
[docs-adapterview]: https://developer.android.com/reference/android/widget/AdapterView.html#setEmptyView(android.view.View)
[docs-popupwindow]: https://developer.android.com/reference/android/widget/PopupWindow.html
[docs-clicklistener]: javadoc/com/algolia/instantsearch/ui/utils/ItemClickSupport.OnItemClickListener.html
[interfaces]: javadoc/com/algolia/instantsearch/model/package-frame.html

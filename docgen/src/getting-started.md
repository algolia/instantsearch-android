---
title: Getting started
layout: main.pug
name: getting-started
category: intro
withHeadings: true
navWeight: 100
---

*This guide will walk you through the few steps needed to start a project with InstantSearch Android.  
We will start from an empty Android project, and create from scratch a full search interface!*

## Before we start
To use InstantSearch Android, you need an Algolia account. You can create one by clicking here, or use the following credentials:
- APP ID: `latency`
- Search API Key: `3d9875e51fbd20c7754e65422f7ce5e1`
- Index name: `bestbuy`

## Create a new Project and add InstantSearch Android
In Android Studio, create a new Project: 
- On the Target screen, select **Phone and Tablet**
- On the Add an Activity screen, select **Empty Activity**

in your app's `build.gradle`, add the following dependency:  
```groovy
compile 'com.algolia:instantsearch-android:0.5.1'
```

## Build the User Interface and display your data: Hits and helpers 

InstantSearch Android is based on a system of [widgets][widgets] that communicate when an user interacts with your app. The first widget we'll add is [Hits][widgets-hits], which will display your search results (and as your app has no input for now, we will trigger the search programmatically).


- To keep this guide simple, we'll replace the main activity's layout by a vertical `LinearLayout`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    android:id="@+id/activity_main"
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
</LinearLayout>
```

- You can then add the `Hits` widget to your layout:
```xml
<com.algolia.instantsearch.ui.views.Hits
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        algolia:itemLayout="@layout/hits_item"/>
```
Its `itemLayout` attribute specifies the layout that will be used to display each item of the results. This layout will contain a view for each attribute of our data that we want to display.
- Let's create a new layout called `hits_item.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="horizontal"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/product_image"/>
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/product_name"/>
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/product_price"/>
</LinearLayout>
```

- Add databinding:
```groovy
android {
    dataBinding.enabled true
    //...
}
```
- Wrap layout in `<layout>` and add databinding attributes:

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:algolia="http://schemas.android.com/apk/res-auto">
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
                  android:layout_width="match_parent"
                  android:layout_height="match_parent"
                  android:orientation="horizontal">

        <ImageView
            android:id="@+id/product_image"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            algolia:attribute="{'thumbnailImage'}"/>

        <TextView
            android:id="@+id/product_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            algolia:attribute="{'name'}"/>

        <TextView
            android:id="@+id/product_price"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            algolia:attribute="{'price'}"/>
    </LinearLayout>
</layout>
```

- Setup Searcher and InstantSearchHelper

```java
public class MainActivity extends AppCompatActivity {
    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_SEARCH_API_KEY = "3d9875e51fbd20c7754e65422f7ce5e1";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Searcher searcher = new Searcher(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
        InstantSearchHelper helper = new InstantSearchHelper(this, searcher);
        helper.search(); // First empty search to display default results
    }
}
```
## Search your data: the SearchBox

- Add SearchBox to `main_activity.xml`
```xml
<com.algolia.instantsearch.ui.views.SearchBox
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

## Help the user understand your results: Highlighting

## Filter your data: the RefinementList

[widgets]: /widgets.html
[widgets-hits]: /widgets.html#hits

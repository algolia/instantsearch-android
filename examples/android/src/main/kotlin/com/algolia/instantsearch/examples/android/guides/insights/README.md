# Send Events with InstantSearch Android 

The InstantSearch Insights Android library allows developers to send `click`, `conversion`, and `view` events. 
For search-related events, it correlates the event with the `queryID`s generated when you’ve set the `clickAnalytics` 
parameter to `true`.

## How to run this example

### 1. Clone this repository

```sh
git clone git@github.com:algolia/instantsearch-android.git
```

### 2. Build the project

```sh
./gradlew :examples:android:assembleDebug
```

### 3. Install the app

```sh
./gradlew :examples:android:installDebug
```

### 4. Launch the example

The example is under `Guides > Insights`.

## Additional resources
Learn more about [sending events with InstantSearch Android](https://www.algolia.com/doc/guides/building-search-ui/going-further/send-insights-events/android/) in the Algolia documentation.

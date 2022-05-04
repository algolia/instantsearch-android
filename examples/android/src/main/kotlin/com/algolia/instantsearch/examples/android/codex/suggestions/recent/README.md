# Query suggestions & Recent searches implementation example

Search experience consisting of two sections:
- Recent searches
- Query suggestions list

When a search query submitted, it's added to the recent searches list.
Selection of a suggestion or a recent search input updates the search query.

<img src="/docs/img/codex/query_suggestions_recent.gif" width="300"/>

## How to run this example

### 1. Clone this repository

```sh
git clone git@github.com:algolia/instantsearch-android.git
```

### 2. Build the project

```sh
./gradlew :examples:assembleDebug
```

### 3. Install the app

```sh
./gradlew :examples:installDebug
```

### 4. Launch the example

The example is under `Code Exchange > Query Suggestions Recent`.

## Additional resources
Learn more about [Query Suggestions in InstantSearch Android](https://www.algolia.com/doc/guides/building-search-ui/ui-and-ux-patterns/query-suggestions/android/) in the Algolia documentation.

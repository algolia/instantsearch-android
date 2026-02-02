# Migrate from 3.x to 4.x

The library version `4.0` uses Kotlin version `2.2` and Algolia Kotlin API Client `3.37`. Below are the steps to migrate.

## Table of Contents

- [Requirements](#requirements)
- [Kotlin API Client](#kotlin-api-client)
- [Removed Features](#removed-features)
- [Breaking Changes](#breaking-changes)

## Requirements

InstantSearch Android 4.x has updated minimum requirements:

| Requirement    | 3.x       | 4.x       |
|----------------|:----------|:----------|
| Kotlin         | 1.9+      | 2.2+      |
| Android minSdk | 21        | 23        |
| Android compileSdk | 33    | 35        |
| Ktor           | 2.3       | 3.3       |
| Algolia Client | 2.x       | 3.x       |

## Kotlin API Client

The Algolia Kotlin API Client has been upgraded from version `2.x` to `3.x`. This is a major version upgrade with significant changes.

### Key Changes

#### Import Paths
Some classes may have moved to different packages. Update your imports accordingly.

#### Data Classes
The primitive wrapper data classes have been removed:
- `Attribute` - Use `String` directly
- `ObjectID` - Use `String` directly  
- `AppID` - Use `String` directly

**Before (3.x):**
```kotlin
val objectID = ObjectID("my-object-id")
val attribute = Attribute("category")
```

**After (4.x):**
```kotlin
val objectID = "my-object-id"
val attribute = "category"
```

#### API Method Changes
Some API methods and response structures have changed. Refer to the [Algolia Kotlin Client 3.x documentation](https://github.com/algolia/algoliasearch-client-kotlin) for details.

## Removed Features

### SearcherAnswers
The Answers feature has been removed as it's no longer supported by Algolia. If you were using `SearcherAnswers`, you'll need to migrate to alternative search approaches:

**Removed:**
- `SearcherAnswers` class
- `SearcherAnswers` filter-state connection
- Related telemetry hooks

### SearcherPlaces
The Places feature has been deprecated and removed. Please refer to [Algolia Places migration guide](https://www.algolia.com/doc/guides/building-search-ui/ui-and-ux-patterns/geo-search/android/) for alternatives.

## Breaking Changes

### Insights Events
The Insights implementation has been updated to use v3 events:
- HTTP repository now sends events via `pushEvents` API
- Filter strings are now parsed into stored filters
- Automatic hits view tracking uses v3 event format

### Facets
- Facet list tracing now uses `FacetHits` instead of older structures
- Facets search now serializes `SearchParamsObject` into `params` for SearchForFacetValues requests

### Dependencies
If you're updating your project, make sure to update related dependencies:

```gradle
// Update Kotlin
kotlin = "2.2.0"

// Update Algolia Client
implementation "com.algolia:algoliasearch-client-kotlin:3.37.2"

// Update Ktor if using directly
ktor = "3.3.3"

// Update Compose if using
androidx.compose.ui = "1.10.0"
```

### Migration Steps

1. **Update dependencies** in your `build.gradle` or `gradle/libs.versions.toml`
2. **Update minimum SDK version** to 23 in your `build.gradle`
3. **Remove wrapper classes**: Replace `ObjectID`, `Attribute`, `AppID` with `String`
4. **Remove Answers usage**: If using `SearcherAnswers`, migrate to standard search
5. **Remove Places usage**: If using `SearcherPlaces`, migrate to alternative geo-search solutions
6. **Update API calls**: Review and update any direct Algolia API client calls according to client 3.x changes
7. **Test thoroughly**: The new versions include significant internal changes, so comprehensive testing is recommended

For more details on specific changes, refer to the [CHANGELOG](../../CHANGELOG.md).

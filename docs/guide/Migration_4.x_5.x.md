# Migrate from 4.x to 5.x

The library version `5.0` targets Android 17 (API level 37) and uses Kotlin `2.4`, Android Gradle Plugin `9` and Compose `1.12`. There are no changes to the InstantSearch API itself: the major version bump reflects the new build requirements that every consuming app has to meet. Below are the steps to migrate.

## Table of Contents

- [Requirements](#requirements)
- [Build configuration](#build-configuration)
- [Dependencies](#dependencies)
- [Breaking changes](#breaking-changes)
- [Migration checklist](#migration-checklist)

## Requirements

InstantSearch Android 5.x has updated minimum requirements:

| Requirement            | 4.x       | 5.x       |
|------------------------|:----------|:----------|
| Kotlin                 | 2.2+      | 2.4+      |
| Android Gradle Plugin  | 8.x       | 9.1.1+    |
| Gradle                 | 8.x       | 9.x       |
| Android `minSdk`       | 23        | 23        |
| Android `compileSdk`   | 35        | 37        |
| Java bytecode (Android artifacts) | 8 | 11      |
| Java bytecode (JVM artifacts)     | 8 | 8       |
| Compose (`instantsearch-compose`) | 1.10 | 1.12 |
| Ktor                   | 3.3       | 3.6       |
| Algolia Kotlin client  | 3.x       | 3.x       |

The published AARs declare `minCompileSdk = 37`. Gradle refuses to resolve them from a module with a lower `compileSdk`, so this is the first thing to update.

## Build configuration

### `compileSdk` 37 and AGP 9

Set `compileSdk` to `37` in every Android module that depends on InstantSearch, and update AGP to `9.1.1` or later (the first AGP release that supports API level 37). AGP 9 requires Gradle 9.

```toml
# gradle/libs.versions.toml
[versions]
agp = "9.3.3"
kotlin = "2.4.20"
```

```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.5.0-bin.zip
```

If you were still on AGP 8, read the official [AGP 9.0 migration notes](https://developer.android.com/build/releases/agp-9-0-0-release-notes). The main points that affect a typical InstantSearch app:

- Kotlin support is built into AGP. Remove the `org.jetbrains.kotlin.android` (`kotlin-android`) plugin from your Android modules.
- `kotlinOptions {}` is replaced by `kotlin { compilerOptions {} }`.
- `composeOptions { kotlinCompilerExtensionVersion }` is no longer needed; apply `org.jetbrains.kotlin.plugin.compose` instead (already required since Kotlin 2.0).

### Java 11 bytecode

The Android artifacts (`instantsearch-android`, `instantsearch-compose`, `instantsearch-insights-android`, `instantsearch-android-paging3`, `instantsearch-android-loading`, `instantsearch-utils-android`) are compiled to Java 11 bytecode, which is what current AndroidX libraries ship as well. Because InstantSearch exposes public `inline` functions (for example `QueryRuleCustomDataConnector`, `MutableFilters.add`, `ViewGroup.inflate`), Kotlin refuses to inline Java 11 bytecode into a module compiled for Java 8:

```
Cannot inline bytecode built with JVM target 11 into bytecode that is being built with JVM target 1.8
```

Set both the Java and Kotlin targets to 11 in your Android modules. `minSdk` stays at 23: D8 desugars Java 11 language features for older devices.

**Before (4.x):**
```kotlin
android {
    compileSdk = 35
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
```

**After (5.x):**
```kotlin
android {
    compileSdk = 37
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}
```

The pure-JVM artifacts (`instantsearch-core`, `instantsearch-coroutines-extensions`, and the `-jvm` variants) still target Java 8, so server-side or desktop consumers are not affected.

## Dependencies

Update the InstantSearch artifacts to `5.x`:

```groovy
implementation "com.algolia:instantsearch-android:5.+"
implementation "com.algolia:instantsearch-compose:5.+"           // optional
implementation "com.algolia:instantsearch-android-paging3:5.+"   // optional
implementation "com.algolia:instantsearch-android-loading:5.+"   // optional
implementation "com.algolia:instantsearch-insights-android:5.+"  // optional
```

Transitive dependencies that moved to a new major or minor line and may conflict with versions pinned in your app:

| Dependency | 4.x | 5.x | Notes |
|---|---|---|---|
| Compose UI | 1.10.0 | 1.12.1 | Compose 1.12 itself requires `compileSdk 37`. Gradle resolves to 1.12.1 even if your Compose BOM maps to an older release; move to a [BOM version](https://developer.android.com/develop/ui/compose/bom/bom-mapping) that maps to 1.12.1 or later to keep your versions explicit |
| Compose Material | 1.3.1 | 1.12.1 | `material` now follows the Compose UI version |
| Compose Material icons | bundled with `material` | 1.7.8 (`material-icons-core`) | Icons are no longer a transitive dependency of `material`; `instantsearch-compose` declares `material-icons-core` explicitly. If your app uses `Icons.*` without declaring the dependency, add `androidx.compose.material:material-icons-core` (or `-extended`) yourself |
| Paging | 3.1.1 | 3.5.1 | `paging-compose` 3.2+ removed `items(LazyPagingItems)`. Use `items(count = pagingItems.itemCount) { index -> pagingItems[index] }` |
| WorkManager (Insights) | 2.11.0 | 2.11.2 | Pinned to 2.11.x, the last line that supports `minSdk 23` |
| Kotlin coroutines | 1.10.2 | 1.11.0 | |
| kotlinx-serialization | 1.5.1 | 1.11.0 | |
| Ktor | 3.3.3 | 3.6.0 | Transitive through the Algolia Kotlin client |
| AndroidX core-ktx / appcompat / recyclerview / swiperefreshlayout | 1.9.0 / 1.6.1 / 1.2.1 / 1.1.0 | 1.19.1 / 1.8.0 / 1.4.0 / 1.2.0 | |

## Breaking changes

### Non-null `onCheckedChanged` parameters

`FilterMapViewRadioGroup` and `FilterToggleViewCompoundButton` implement the platform `OnCheckedChangeListener` interfaces. API level 37 annotated these callbacks as non-null, so the overrides changed accordingly:

**Before (4.x):**
```kotlin
override fun onCheckedChanged(group: RadioGroup?, checkedId: Int)
override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean)
```

**After (5.x):**
```kotlin
override fun onCheckedChanged(group: RadioGroup, checkedId: Int)
override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean)
```

This only affects you if you wrote your own `OnCheckedChangeListener` implementations against the old nullable signatures: with `compileSdk 37` the Kotlin compiler reports `'onCheckedChanged' overrides nothing` until you drop the `?`.

### No API changes

Widgets, connectors, view models, searchers, filters and Insights keep the 4.x API. Code that compiled against 4.1 compiles against 5.0 once the build configuration above is in place.

## Migration checklist

1. **Update the toolchain**: Gradle 9.x, AGP 9.1.1+, Kotlin 2.4+.
2. **Set `compileSdk = 37`** in every Android module that depends on InstantSearch.
3. **Set Java/Kotlin targets to 11** (`compileOptions` and `jvmTarget`) in those modules.
4. **Remove the `kotlin-android` plugin** and migrate `kotlinOptions` to `kotlin { compilerOptions }` if you upgraded from AGP 8.
5. **Bump InstantSearch artifacts** to `5.+`.
6. **Align Compose**: BOM `2026.09.00`+ or Compose `1.12.1`+, and declare `material-icons-core`/`-extended` if your code uses `Icons.*`.
7. **Fix `OnCheckedChangeListener` overrides** that still use nullable parameters.
8. **Review Android 17 behavior changes** for your app when you flip `targetSdk` to 37, in particular the large-screen changes (orientation and resizability restrictions are ignored on screens of 600dp and wider) and the local network permission. The library itself declares no Activity, Service or permission, so it is not affected. See the [Android 17 behavior changes](https://developer.android.com/about/versions/17/behavior-changes-17).
9. **Run your test suite** on an Android 17 emulator or device.

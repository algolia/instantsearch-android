---
name: project-conventions
description: Coding standards, API conventions, build patterns, and project structure rules for the InstantSearch Android project. Use when writing new code, reviewing code, checking conventions, or when unsure about project standards.
---

# Project Conventions

## API Visibility

### Explicit API mode

All library modules enforce `explicitApi()` (KMP) or `-Xexplicit-api=strict` (Android). Every declaration must have an explicit visibility modifier.

```kotlin
public class MyClass             // explicit — required
public fun doSomething()         // explicit — required
internal fun helperMethod()      // explicit — required
private val cache = mutableMapOf<String, Any>()  // explicit — required
```

### Custom annotations

| Annotation | Purpose | Usage |
|------------|---------|-------|
| `@ExperimentalInstantSearch` | Marks unstable public API | Apply to new APIs that may change |
| `@InternalInstantSearch` | Marks internal API not for public use | Apply to APIs that must be public for cross-module access but should not be used externally |

Both are defined in `instantsearch-utils` at `com.algolia.instantsearch`.

### Internal packages

Implementation details go in `*.internal` sub-packages:

```
com.algolia.instantsearch.filter.facet/           # public API
com.algolia.instantsearch.filter.facet.internal/   # implementation
```

## Package structure

Base package: `com.algolia.instantsearch`

| Module | Package prefix |
|--------|---------------|
| `instantsearch-core` | `com.algolia.instantsearch.core.*` |
| `instantsearch` | `com.algolia.instantsearch.*` (common), `com.algolia.instantsearch.android.*` (Android) |
| `instantsearch-insights` | `com.algolia.instantsearch.insights.*` |
| `instantsearch-compose` | `com.algolia.instantsearch.compose.*` |
| `instantsearch-utils` | `com.algolia.instantsearch.*` (shared utilities) |
| Extensions | `com.algolia.instantsearch.android.<ext>` or `com.algolia.instantsearch.coroutines.*` |

## Build configuration

### Android settings

| Property | Value |
|----------|-------|
| `compileSdk` | `35` |
| `minSdk` | `23` |
| Java source/target | `VERSION_1_8` |
| JVM target | `JVM_1_8` |
| `isIncludeAndroidResources` | `true` |
| `isReturnDefaultValues` | `true` |

### Resource prefixes

| Module | Prefix |
|--------|--------|
| `instantsearch` | `alg_is_` |
| `instantsearch-insights` | `alg_is_insights_` |
| `instantsearch-compose` | `alg_is_compose_` |

New modules with Android resources should follow: `alg_is_<suffix>_`

### Opt-ins

Common opt-ins across modules (configured in `languageSettings` or `freeCompilerArgs`):

```
kotlin.RequiresOptIn
com.algolia.instantsearch.InternalInstantSearch
com.algolia.instantsearch.ExperimentalInstantSearch
kotlinx.coroutines.ExperimentalCoroutinesApi
```

Add only what the module needs.

## Dependencies

### Version catalog

All dependencies are managed in `gradle/libs.versions.toml`. Always use catalog references:

```kotlin
implementation(libs.kotlinx.coroutines.core)   // correct
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")  // avoid
```

When adding new dependencies, add them to the version catalog first.

### Dependency scope rules

| Scope | When to use |
|-------|-------------|
| `api` | Dependency types appear in the module's public API |
| `implementation` | Internal usage only |
| `testImplementation` | Test-only dependencies |

## Kotlin Multiplatform

### Source set layout

```
src/
├── commonMain/     # Shared code (no platform imports)
├── commonTest/     # Shared tests
├── jvmMain/        # JVM-specific (optional)
├── jvmTest/        # JVM tests
├── androidMain/    # Android-specific
└── androidUnitTest/  # Android unit tests (Robolectric)
```

### Platform-specific code

Use `expect`/`actual` or source-set-specific files. Keep `commonMain` free of platform imports.

## Testing

### Test file naming

- Prefix with `Test`: `TestFacetListConnectSearcher`, `TestFacetListViewModel`
- Or suffix with `Test`: `InsightsTest`, `FacetListStateTest`

### Test dependencies by source set

| Source set | Dependencies |
|------------|-------------|
| `commonTest` | `kotlin-test-common`, `kotlin-test-annotations-common`, `kotlinx-coroutines-test` |
| `jvmTest` | `kotlin-test-junit` |
| `androidUnitTest` | `kotlin-test-junit`, `robolectric`, `mockk`, `androidx.test.*` |
| Compose `test` | `kotlin-test-junit`, `kotlinx-coroutines-test` |

### Test utilities

- Ktor mock client for network tests: `libs.test.ktor.client.mock`
- Turbine for Flow testing: `libs.test.turbine`
- MockK for mocking: `libs.test.mockk`

## Publishing

### Maven coordinates

- Group: `com.algolia` (from `gradle.properties` `GROUP`)
- Version: from `gradle.properties` `VERSION_NAME`
- Artifact ID: from module's `gradle.properties` `POM_ARTIFACT_ID`

### Publishing plugin

All publishable modules use `com.vanniktech.maven.publish`. The `gradle.properties` in each module defines `POM_NAME` and `POM_ARTIFACT_ID`.

## Code formatting

### Spotless

Applied globally to all subprojects. Rules:
- Target: `**/*.kt`
- Trim trailing whitespace
- End files with newline

Run `./gradlew spotlessCheck` to verify, `./gradlew spotlessApply` to auto-fix.

## Documentation

### KDoc

All public API should have KDoc with:
- Class/function description
- `@param` for constructor/function parameters
- Link to Algolia docs where applicable: `[Documentation](https://www.algolia.com/doc/...)`

### No unnecessary comments

Do not add comments that merely restate what the code does. Comments should explain non-obvious intent or constraints.

## Patterns quick reference

| Pattern | Description |
|---------|-------------|
| `Connection` / `AbstractConnection` | Connect/disconnect lifecycle for wiring components |
| `SubscriptionValue<T>` | Observable value with subscription support |
| `Presenter<I, O>` | Functional transform `(I) -> O` for display |
| `Connector` | Composes multiple `Connection`s into a single unit |
| View/ViewModel split | View = UI contract, ViewModel = state + logic |
| Compose `*State` | Interface + factory function, internal `*StateImpl` |

## CI

- **Lint:** `./gradlew spotlessCheck`
- **Test:** `./gradlew runDebugUnitTest`
- **Publish:** `./gradlew publishAndReleaseToMavenCentral` (manual workflow dispatch)

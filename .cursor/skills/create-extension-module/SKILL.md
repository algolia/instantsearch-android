---
name: create-extension-module
description: Scaffold a new extension module under extensions/ for the InstantSearch Android project. Use when the user wants to create an extension, add a new plugin integration, add a new Android library extension, or add third-party library support.
---

# Create Extension Module

Extensions live under `extensions/` and integrate InstantSearch with third-party Android libraries (Paging3, SwipeRefreshLayout, etc.) or provide additional utilities.

## Existing extensions for reference

| Module | Type | Depends on | Integrates with |
|--------|------|------------|-----------------|
| `android-paging3` | Android-only | `:instantsearch` | AndroidX Paging 3 |
| `android-loading` | Android-only | `:instantsearch` | SwipeRefreshLayout |
| `coroutines-extensions` | KMP JVM-only | `:instantsearch-core` | Kotlin Coroutines/Flow |

## Step-by-step

### 1. Create the module directory

```
extensions/<extension-name>/
├── build.gradle.kts
├── gradle.properties
└── src/
```

### 2. Create `build.gradle.kts`

#### Android extension (most common)

```kotlin
plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.android.<extension_suffix>"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        freeCompilerArgs.addAll(listOf(
            "-Xopt-in=com.algolia.instantsearch.ExperimentalInstantSearch",
            "-Xopt-in=com.algolia.instantsearch.InternalInstantSearch",
            "-Xexplicit-api=strict"
        ))
    }
}

dependencies {
    api(project(":instantsearch"))
    // api(libs.androidx.<library>)  // the third-party library
    testImplementation(kotlin("test-junit"))
}
```

#### KMP extension (pure Kotlin, no Android)

```kotlin
plugins {
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
        val commonMain by getting {
            dependencies {
                api(project(":instantsearch-core"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
    }
}
```

### 3. Create `gradle.properties`

```properties
POM_NAME=InstantSearch Android <Extension Name> Extension
POM_ARTIFACT_ID=instantsearch-android-<extension-name>
```

For KMP extensions without Android:

```properties
POM_NAME=InstantSearch <Extension Name> Extensions
POM_ARTIFACT_ID=instantsearch-<extension-name>
```

### 4. Register in `settings.gradle.kts`

Add under the Extensions section:

```kotlin
// Extensions
include(":extensions:android-paging3")
include(":extensions:android-loading")
include(":extensions:coroutines-extensions")
include(":extensions:<new-extension-name>")  // <-- add here
```

### 5. Register in root test task

In root `build.gradle.kts`, add to `runDebugUnitTest`:

```kotlin
dependsOn(":extensions:<name>:testDebugUnitTest")   // Android
dependsOn(":extensions:<name>:jvmTest")              // KMP JVM-only
```

### 6. Create source directories

Android extension:

```
extensions/<name>/src/
├── main/kotlin/com/algolia/instantsearch/android/<package>/
└── test/kotlin/com/algolia/instantsearch/android/<package>/
```

KMP extension:

```
extensions/<name>/src/
├── commonMain/kotlin/com/algolia/instantsearch/<package>/
├── commonTest/kotlin/com/algolia/instantsearch/<package>/
└── jvmTest/kotlin/com/algolia/instantsearch/<package>/
```

### 7. Add new version catalog entries (if needed)

If the extension integrates a new third-party library, add it to `gradle/libs.versions.toml`:

```toml
[libraries]
androidx-<lib> = { group = "...", name = "...", version = "..." }
```

### 8. Verify

```bash
./gradlew :extensions:<name>:assemble
./gradlew spotlessCheck
```

## Checklist

- [ ] Module lives under `extensions/<name>/`
- [ ] `build.gradle.kts` follows project conventions
- [ ] `gradle.properties` has correct `POM_NAME` and `POM_ARTIFACT_ID`
- [ ] Registered in `settings.gradle.kts` under Extensions section
- [ ] Added to `runDebugUnitTest` in root `build.gradle.kts`
- [ ] Third-party library exposed via `api()` (not `implementation()`)
- [ ] `explicitApi()` or `-Xexplicit-api=strict` enabled
- [ ] New dependencies added to version catalog if needed
- [ ] Module compiles successfully

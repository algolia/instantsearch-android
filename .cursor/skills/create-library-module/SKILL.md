---
name: create-library-module
description: Scaffold a new publishable library module for the InstantSearch Android project. Use when the user wants to create a new library module, add a new KMP module, add a new Android library module, or scaffold a new package for publishing.
---

# Create Library Module

This skill guides creation of new publishable library modules in the InstantSearch Android multi-module project.

## Module Types

### 1. KMP (Kotlin Multiplatform) with Android target

Use when the module needs both JVM and Android source sets.

**Examples in project:** `instantsearch`, `instantsearch-insights`, `instantsearch-utils`

### 2. KMP JVM-only

Use when the module has no Android-specific code.

**Example in project:** `instantsearch-core`

### 3. Android-only library

Use when the module only targets Android (e.g. Compose, View-specific widgets).

**Examples in project:** `instantsearch-compose`

## Step-by-step

### 1. Choose a module name and location

- Top-level modules: `<module-name>/` (e.g. `instantsearch-newfeature/`)
- Extension modules: use the `create-extension-module` skill instead

### 2. Create `build.gradle.kts`

Use the appropriate template below. Key conventions:

| Setting | Value |
|---------|-------|
| `compileSdk` | `35` |
| `minSdk` | `23` |
| Java compat | `VERSION_1_8` / `JVM_1_8` |
| Explicit API | Always enabled |
| Publish plugin | `com.vanniktech.maven.publish` |

#### Template A: KMP + Android

```kotlin
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.<module_suffix>"
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

    // resourcePrefix = "alg_is_<suffix>_"  // if module has Android resources
}

kotlin {
    explicitApi()
    jvm()
    androidTarget()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("com.algolia.instantsearch.InternalInstantSearch")
                optIn("com.algolia.instantsearch.ExperimentalInstantSearch")
            }
        }
        commonMain {
            dependencies {
                // Add project dependencies, e.g.:
                // api(project(":instantsearch-core"))
            }
        }
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
            }
        }
        jvmTest {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
        androidMain {
            dependencies {
                // Android-specific deps
            }
        }
        androidUnitTest {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.androidx.runner)
                implementation(libs.test.androidx.ext)
                implementation(libs.test.robolectric)
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}
```

#### Template B: KMP JVM-only

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
                // api(project(":instantsearch-core"))
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}
```

#### Template C: Android-only library

```kotlin
plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.<module_suffix>"
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
        freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict"))
    }
}

dependencies {
    // api(project(":instantsearch"))
    testImplementation(kotlin("test-junit"))
}
```

If the module uses **Compose**, also add:

```kotlin
alias(libs.plugins.compose.compiler)  // in plugins block
```

```kotlin
buildFeatures {
    compose = true
}
```

### 3. Create `gradle.properties`

```properties
POM_NAME=InstantSearch <Human-Readable Name>
POM_ARTIFACT_ID=instantsearch-<artifact-id>
```

### 4. Register in `settings.gradle.kts`

Add `include(":<module-name>")` in the appropriate section:

```kotlin
// Instant Search
include(":instantsearch")
include(":instantsearch-core")
// ... existing modules ...
include(":<new-module-name>")   // <-- add here
```

### 5. Register in root `build.gradle.kts` test task (if applicable)

Add to the `runDebugUnitTest` task:

- KMP module: `dependsOn(":<module>:jvmTest")`
- Android module: `dependsOn(":<module>:testDebugUnitTest")`

### 6. Create source directories

#### KMP + Android

```
<module>/
├── build.gradle.kts
├── gradle.properties
└── src/
    ├── commonMain/kotlin/com/algolia/instantsearch/<package>/
    ├── commonTest/kotlin/com/algolia/instantsearch/<package>/
    ├── androidMain/kotlin/com/algolia/instantsearch/<package>/
    ├── androidUnitTest/kotlin/com/algolia/instantsearch/<package>/
    ├── jvmMain/kotlin/com/algolia/instantsearch/<package>/  (optional)
    └── jvmTest/kotlin/com/algolia/instantsearch/<package>/
```

#### KMP JVM-only

```
<module>/
├── build.gradle.kts
├── gradle.properties
└── src/
    ├── commonMain/kotlin/com/algolia/instantsearch/<package>/
    ├── commonTest/kotlin/com/algolia/instantsearch/<package>/
    └── jvmTest/kotlin/com/algolia/instantsearch/<package>/
```

#### Android-only

```
<module>/
├── build.gradle.kts
├── gradle.properties
└── src/
    ├── main/kotlin/com/algolia/instantsearch/<package>/
    └── test/kotlin/com/algolia/instantsearch/<package>/
```

### 7. Verify

Run `./gradlew :<module-name>:assemble` to confirm the module compiles.
Run `./gradlew spotlessCheck` to confirm formatting.

## Checklist

- [ ] `build.gradle.kts` uses correct template for module type
- [ ] `gradle.properties` has `POM_NAME` and `POM_ARTIFACT_ID`
- [ ] Module registered in `settings.gradle.kts`
- [ ] Module added to `runDebugUnitTest` in root `build.gradle.kts`
- [ ] Source directories created with correct package prefix
- [ ] `explicitApi()` enabled
- [ ] Java/JVM target is 1.8
- [ ] `compileSdk = 35`, `minSdk = 23` (if Android)
- [ ] Dependencies reference version catalog (`libs.*`)
- [ ] Module compiles successfully

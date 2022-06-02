rootProject.name = "instantsearch-android"
enableFeaturePreview("VERSION_CATALOGS")

// Instant Search
include(":instantsearch")
include(":instantsearch-core")
include(":instantsearch-insights")
include(":instantsearch-compose")
include(":instantsearch-utils")
// Extensions
include(":extensions:android-paging3")
include(":extensions:android-loading")
include(":extensions:coroutines-extensions")
// Examples: showcases, guides and codex
include(":examples:android")
include(":examples:wearos")
include(":examples:androidtv")
// Benchmark
include(":benchmark")

val localSettings = file("local.settings.gradle.kts")
if (localSettings.exists()) apply(from = localSettings)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

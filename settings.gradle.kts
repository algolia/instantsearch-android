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
// Examples: showcases
include(":examples:showcase:android-view")
include(":examples:showcase:compose")
// Examples: guides
include(":examples:guides")
// Examples: code exchange
include(":examples:exchange:categories-hits")
include(":examples:exchange:multi-index")
include(":examples:exchange:query-suggestions")
include(":examples:exchange:query-suggestions-categories")
include(":examples:exchange:query-suggestions-hits")
include(":examples:exchange:query-suggestions-recent")
include(":examples:exchange:voice-search")

val localSettings = file("local.settings.gradle.kts")
if (localSettings.exists()) apply(from = localSettings)

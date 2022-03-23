rootProject.name = "instantsearch-android"
enableFeaturePreview("VERSION_CATALOGS")

include(":instantsearch")
include(":instantsearch-core")
include(":instantsearch-insights")
include(":instantsearch-compose")
include(":instantsearch-utils")
include(":extensions:android-paging3")
include(":extensions:android-loading")
include(":extensions:coroutines-extensions")

val localSettings = file("local.settings.gradle.kts")
if (localSettings.exists()) {
    apply(from = localSettings)
}

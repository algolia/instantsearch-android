rootProject.name = "instantsearch-android"
enableFeaturePreview("VERSION_CATALOGS")

include(":instantsearch")
include(":instantsearch-core")
include(":instantsearch-insights")
include(":instantsearch-compose")
include(":instantsearch-telemetry")
include(":instantsearch-utils")

val localSettings = file("local.settings.gradle.kts")
if (localSettings.exists()) {
    apply(from = localSettings)
}

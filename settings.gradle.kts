pluginManagement {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "instantsearch-android"

enableFeaturePreview("GRADLE_METADATA")

include(":instantsearch-core")
include(":instantsearch")

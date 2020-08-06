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

include(":core")
include(":helper")

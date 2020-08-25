pluginManagement {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "instantsearch-android"

include(":instantsearch-android-core")
include(":instantsearch-android")

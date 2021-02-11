import dependency.network.AlgoliaClient
import dependency.network.Coroutines
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.ui.AndroidCore
import dependency.ui.AppCompat
import dependency.ui.MaterialDesign
import dependency.ui.Paging
import dependency.ui.RecyclerView
import dependency.ui.SwipeRefreshLayout

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlinx-serialization")
}

apply(from = "../gradle/gradle-maven-publish.gradle")

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    libraryVariants.all {
        generateBuildConfigProvider.configure { enabled = false }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += listOf(
            "-Xexplicit-api=warning",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=com.algolia.search.ExperimentalAlgoliaClientAPI"
        )
    }

    testOptions {
        unitTests {
            it.isIncludeAndroidResources = true
            it.isReturnDefaultValues = true
        }
    }
}

group = Library.group
version = Library.version

dependencies {
    api(project(":instantsearch-android-core"))
    api(AlgoliaClient())
    api(Ktor("client-okhttp"))
    api(AndroidCore("ktx"))
    api(AppCompat())
    api(RecyclerView())
    api(MaterialDesign())
    api(SwipeRefreshLayout())
    api(Paging())
    api(Coroutines("android"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(Ktor("client-mock-jvm"))
    testImplementation(AndroidTestRunner())
    testImplementation(AndroidTestExt())
    testImplementation(Robolectric())
}

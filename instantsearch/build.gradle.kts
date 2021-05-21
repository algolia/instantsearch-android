import dependency.network.AlgoliaClient
import dependency.network.Coroutines
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Mockk
import dependency.test.Robolectric
import dependency.ui.AndroidCore
import dependency.ui.AppCompat
import dependency.ui.MaterialDesign
import dependency.ui.Paging
import dependency.ui.RecyclerView
import dependency.ui.SwipeRefreshLayout

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    buildTypes {
        val debug by getting {
            matchingFallbacks += "release"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        buildConfig = false
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    // @see: https://youtrack.jetbrains.com/issue/KT-43944
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    explicitApi()
    android {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        publishAllLibraryVariants()
    }
    jvm {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        testRuns["test"].executionTask.configure { useJUnit() }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("com.algolia.search.ExperimentalAlgoliaClientAPI")
        }
        val commonMain by getting {
            dependencies {
                api(project(":instantsearch-core"))
                api(project(":instantsearch-insights"))
                api(AlgoliaClient())
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Coroutines("test"))
                implementation(Ktor("client-mock"))

            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Mockk())
                implementation(Ktor("client-mock-jvm"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(Ktor("client-okhttp"))
                api(AndroidCore("ktx"))
                api(AppCompat())
                api(RecyclerView())
                api(MaterialDesign())
                api(SwipeRefreshLayout())
                api(Paging())
                api(Coroutines("android"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(AndroidTestRunner())
                implementation(AndroidTestExt())
                implementation(Robolectric())
                implementation(Mockk())
            }
        }
    }
}

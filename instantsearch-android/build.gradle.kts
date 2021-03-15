import dependency.network.*
import dependency.test.*
import dependency.ui.*

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    if ("UnitTest" !in name) {
        kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
    }
}

dependencies {
    api(project(":instantsearch-android-core"))
    api(project(":instantsearch-insights"))
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
    testImplementation(Coroutines("test"))
    testImplementation(Mockk())
}

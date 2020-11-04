import dependency.android.AndroidArchCore
import dependency.async.Coroutines
import dependency.async.LiveData

plugins {
    id("com.android.library")
    id("kotlin-android")
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
        freeCompilerArgs += listOf("-Xexplicit-api=warning")
    }

    testOptions {
        unitTests {
            it.isIncludeAndroidResources = true
            it.isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(project(":instantsearch-android"))
    implementation(LiveData())
    implementation(Coroutines("core"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(Coroutines("test"))
    testImplementation(AndroidArchCore("testing"))
}

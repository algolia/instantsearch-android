plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release"){
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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

    resourcePrefix = "alg_is_"
}

kotlin {
    explicitApi()
    android {
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
    }
    jvm {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        testRuns["test"].executionTask.configure { useJUnit() }
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("com.algolia.search.ExperimentalAlgoliaClientAPI")
            languageSettings.optIn("com.algolia.instantsearch.Internal")
        }
        val commonMain by getting {
            dependencies {
                api(project(":instantsearch-core"))
                api(project(":instantsearch-insights"))
                implementation(project(":instantsearch-utils"))
                implementation(project(":instantsearch-telemetry"))
                implementation(libs.algolia.client)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
                implementation(libs.test.ktor.client.mock)
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":instantsearch-telemetry"))
                api(libs.ktor.client.okhttp)
                api(libs.androidx.core)
                api(libs.androidx.appcompat)
                api(libs.androidx.swiperefreshlayout)
                api(libs.androidx.paging)
                api(libs.kotlinx.coroutines.android)
                api(libs.google.material)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.androidx.runner)
                implementation(libs.test.androidx.ext)
                implementation(libs.test.robolectric)
                implementation(libs.test.mockk)
            }
        }
    }
}

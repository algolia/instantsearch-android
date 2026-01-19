plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.android"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    resourcePrefix = "alg_is_"
}

kotlin {
    explicitApi()
    jvm()
    androidTarget()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("com.algolia.instantsearch.InternalInstantSearch")
                optIn("com.algolia.instantsearch.ExperimentalInstantSearch")
                optIn("com.algolia.search.ExperimentalAlgoliaClientAPI")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.serialization.InternalSerializationApi")
            }
        }
        commonMain {
            dependencies {
                api(project(":instantsearch-core"))
                api(project(":instantsearch-insights"))
                api(project(":instantsearch-utils"))
                implementation(project(":migration2to3"))
                implementation(libs.algolia.client)
                implementation(libs.algolia.telemetry)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
                implementation(libs.test.ktor.client.mock)
            }
        }
        jvmTest {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.mockk)
            }
        }
        androidMain {
            dependencies {
                api(libs.ktor.client.okhttp)
                api(libs.androidx.core)
                api(libs.androidx.appcompat)
                api(libs.kotlinx.coroutines.android)
                api(libs.google.material)
            }
        }
        androidNativeTest {
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

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()
    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }
    android {
        namespace = "com.algolia.instantsearch.android"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            // Current AndroidX artifacts (core, appcompat, work, ...) ship Java 11
            // bytecode, so the Android target must be compiled for JVM 11 too.
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
        // Host-side (Robolectric) tests live in `src/androidHostTest`.
        withHostTest {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("com.algolia.instantsearch.InternalInstantSearch")
                optIn("com.algolia.instantsearch.ExperimentalInstantSearch")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.serialization.InternalSerializationApi")
            }
        }
        commonMain {
            dependencies {
                api(project(":instantsearch-core"))
                api(project(":instantsearch-insights"))
                api(project(":instantsearch-utils"))
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
        getByName("androidHostTest") {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.androidx.runner)
                implementation(libs.test.androidx.ext)
                implementation(libs.test.robolectric)
                implementation(libs.test.mockk)
                implementation(libs.test.coroutines)
            }
        }
    }
}

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
        namespace = "com.algolia.instantsearch.insights"
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
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlinx.serialization.InternalSerializationApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("com.algolia.instantsearch.InternalInstantSearch")
            }
        }

        commonMain {
            dependencies {
                implementation(project(":instantsearch-utils"))
                implementation(project(":instantsearch-core"))
                api(libs.algolia.client)
                api(libs.ktor.client.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
            }
        }
        named("jvmMain") {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(libs.logback.classic)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.androidx.core)
                implementation(libs.androidx.work)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        named("androidHostTest") {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.androidx.runner)
                implementation(libs.test.androidx.ext)
                implementation(libs.test.robolectric)
                implementation(libs.test.ktor.client.mock)
                implementation(libs.test.androidx.work)
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

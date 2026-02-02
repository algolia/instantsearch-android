plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.insights"
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
        all {
            it.testLogging {
                events("failed")
                setExceptionFormat("full")
            }
        }
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    resourcePrefix = "alg_is_insights_"
}

kotlin {
    explicitApi()
    androidTarget ()
    jvm()
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
        named("androidUnitTest") {
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.agentstudio"
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

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    resourcePrefix = "alg_is_agentstudio_"
}

kotlin {
    explicitApi()
    androidTarget()
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines.core)
                api(libs.ktor.client.serialization.json)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
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
        named("jvmMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        named("androidUnitTest") {
            dependencies {
                implementation(libs.test.kotlin.junit)
                implementation(libs.test.androidx.runner)
                implementation(libs.test.androidx.ext)
                implementation(libs.test.robolectric)
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict"))
    }
}

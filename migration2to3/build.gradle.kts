plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.migration"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
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
            java.setSrcDirs(emptyList<String>())
            kotlin.setSrcDirs(emptyList<String>())
        }
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
            kotlin.srcDirs("src/main/java")
            dependencies {
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
    }
}

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
        val release by getting {
            isMinifyEnabled = false
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
        publishLibraryVariantsGroupedByFlavor = true
    }
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
        val commonMain by getting {
            dependencies {
                api(libs.algolia.client)
            }
        }
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(libs.slf4j)
                implementation(libs.ktor.client.okhttp)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.logback.classic)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.androidx.core)
                implementation(libs.androidx.work)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.test.androidx.runner)
                implementation(libs.test.androidx.ext)
                implementation(libs.test.robolectric)
                implementation(libs.test.ktor.client.mock)
                implementation(libs.test.androidx.work)
            }
        }
    }
}

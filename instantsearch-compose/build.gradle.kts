import dependency.compose.Material
import dependency.compose.Paging
import dependency.compose.UI

plugins {
    id("com.android.library")
    id("kotlin-android")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        useIR = true
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    buildFeatures {
        buildConfig = false
        compose = true
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    if ("UnitTest" !in name) {
        kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
    }
}

dependencies {
    api(project(":instantsearch-android-runtime"))
    implementation(UI())
    implementation(Paging())
    implementation(Material())
}

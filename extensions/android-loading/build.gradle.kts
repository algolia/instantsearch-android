plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.android.loading"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

// AGP 9 built-in Kotlin: compiler options are configured here instead of
// through the `kotlin-android` plugin.
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict"))
    }
}

dependencies {
    api(project(":instantsearch"))
    api(libs.androidx.swiperefreshlayout)
    testImplementation(kotlin("test-junit"))
    testImplementation(libs.test.androidx.runner)
    testImplementation(libs.test.androidx.ext)
    testImplementation(libs.test.robolectric)
}

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.android.loading"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
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

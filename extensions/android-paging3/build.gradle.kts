plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.android.paging3"
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
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(listOf(
            "-Xopt-in=com.algolia.instantsearch.ExperimentalInstantSearch",
            "-Xopt-in=com.algolia.instantsearch.InternalInstantSearch",
            "-Xexplicit-api=strict"
        ))
    }
}

dependencies {
    api(project(":instantsearch"))
    api(libs.androidx.paging3)
    testImplementation(kotlin("test-junit"))
}

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.android.paging3"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += listOf(
            "-Xopt-in=com.algolia.instantsearch.ExperimentalInstantSearch",
            "-Xopt-in=com.algolia.instantsearch.InternalInstantSearch",
            "-Xexplicit-api=strict"
        )
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}


dependencies {
    api(project(":instantsearch"))
    api(libs.androidx.paging3)
    testImplementation(kotlin("test-junit"))
}

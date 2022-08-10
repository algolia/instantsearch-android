plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.compose"
    compileSdk = 32

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
        freeCompilerArgs += listOf("-Xexplicit-api=strict")
    }

    buildFeatures {
        buildConfig = false
        compose = true
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    resourcePrefix = "alg_is_compose_"
}

tasks.withType<Test> {
    useJUnit()
}

dependencies {
    api(project(":instantsearch"))
    api(project(":instantsearch-utils"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    testImplementation(kotlin("test-junit"))
}

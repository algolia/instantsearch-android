plugins {
    id("com.android.library")
    id("kotlin-android")
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.compose"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    resourcePrefix = "alg_is_compose_"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict", "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"))
    }
}

tasks.withType<Test> {
    useJUnit()
}

dependencies {
    api(project(":instantsearch"))
    api(project(":instantsearch-utils"))
    implementation(project(":migration2to3"))
    implementation(libs.algolia.telemetry)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.coroutines)
}

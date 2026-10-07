plugins {
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.algolia.instantsearch.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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

// AGP 9 built-in Kotlin: compiler options are configured here instead of
// through the `kotlin-android` plugin.
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict", "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"))
    }
}

tasks.withType<Test> {
    useJUnit()
}

dependencies {
    api(project(":instantsearch"))
    api(project(":instantsearch-utils"))
    implementation(libs.algolia.telemetry)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.core)
    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.coroutines)
}

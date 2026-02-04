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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
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
    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.coroutines)
}

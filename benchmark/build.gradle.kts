plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.algolia.instantsearch.benchmark"
    compileSdk = 32

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}

dependencies {
    implementation(project(":instantsearch-compose"))

    androidTestImplementation("androidx.benchmark:benchmark-junit4:1.0.0")

    androidTestImplementation("androidx.activity:activity-compose:1.4.0")
    androidTestImplementation("androidx.compose.ui:ui:1.1.1")
    androidTestImplementation("androidx.compose.material:material:1.1.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:rules:1.4.0")
}

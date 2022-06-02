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
}

dependencies {
    implementation(project(":instantsearch"))

    androidTestImplementation("androidx.activity:activity-ktx:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("androidx.benchmark:benchmark-junit4:1.0.0")
}

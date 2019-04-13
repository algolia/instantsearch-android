plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(28)
        applicationId = "com.algolia.instantsearch.sample"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    buildTypes {
        getByName("release") {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        // FIXME: Solve these "More than one file was found with OS independent path 'META-INF/*' in the lib via Proguard
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/kotlinx-io.kotlin_module")
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-io.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/ktor-client-logging.kotlin_module")
        exclude("META-INF/algoliasearch-client-kotlin.kotlin_module")
        exclude("META-INF/ktor-client-core.kotlin_module")
    }
}
//
dependencies {
    implementation(project(":helper"))
    implementation(kotlin("stdlib-jdk8"))

    // AndroidX
    implementation(AppCompat())
    implementation(Glide())
    implementation(ContraintLayout())
    implementation(RecyclerView())
    implementation(AndroidCore("ktx"))

    androidTestImplementation(AndroidTest())
    androidTestImplementation(Espresso("core"))
}
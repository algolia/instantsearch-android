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
    implementation("androidx.appcompat:appcompat:1.1.0-alpha04")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.core:core-ktx:1.0.1")

    // Glide
//    implementation 'com.github.bumptech.glide:glide:4.9.0'
//    annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'

//    testImplementation 'junit:junit:4.12'
//    androidTestImplementation 'androidx.test:runner:1.2.0-alpha03'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0-alpha03'
}
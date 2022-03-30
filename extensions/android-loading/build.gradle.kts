plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xexplicit-api=strict"
        )
    }

    buildFeatures {
        buildConfig = false
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
        }
    }
}


dependencies {
    api(project(":instantsearch-core"))
    api(libs.androidx.swiperefreshlayout)
    testImplementation(project(":instantsearch"))
    testImplementation(kotlin("test-junit"))
    testImplementation(libs.test.androidx.runner)
    testImplementation(libs.test.androidx.ext)
    testImplementation(libs.test.robolectric)
}

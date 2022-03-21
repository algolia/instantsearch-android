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
        getByName("release"){
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
        compose = true
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    resourcePrefix = "alg_is_compose_"

    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

tasks.withType<Test> {
    useJUnit()
}

dependencies {
    api(project(":instantsearch"))
    api(project(":instantsearch-utils"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.paging)
    implementation(libs.androidx.compose.material)
    testImplementation(kotlin("test-junit"))
}

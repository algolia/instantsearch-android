plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        buildConfig = false
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    // @see: https://youtrack.jetbrains.com/issue/KT-43944
    configurations {
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    explicitApi()
    android {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
    }
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
        val commonMain by getting {
            dependencies {
                api(dependency.network.AlgoliaClient())
            }
        }
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.30")
                implementation(dependency.network.Ktor("client-okhttp"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("ch.qos.logback:logback-classic:1.2.3")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(dependency.network.Ktor("client-okhttp"))
                implementation(dependency.ui.AndroidCore("ktx"))
                implementation(dependency.lib.Work("runtime-ktx"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
                implementation(dependency.test.AndroidTestRunner())
                implementation(dependency.test.AndroidTestExt())
                implementation(dependency.test.Robolectric())
                implementation(dependency.network.Ktor("client-mock-jvm"))
                implementation(dependency.lib.Work("testing"))
            }
        }
    }
}

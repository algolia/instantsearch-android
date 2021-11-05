import com.google.protobuf.gradle.*

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.protobuf") version "0.8.17"
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

    protobuf {
        protoc {
            artifact = "com.google.protobuf:protoc:3.19.1"
        }
        generateProtoTasks {
            all().forEach { task ->
                task.builtins {
                    //id("java") { option("lite") }
                }
            }
        }
    }
}

kotlin {
    //explicitApi()
    android {
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
    }
    jvm {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        testRuns["test"].executionTask.configure { useJUnit() }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("com.algolia.search.ExperimentalAlgoliaClientAPI")
        }
        val commonMain by getting {
            dependencies {
                api("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.test.coroutines)
                implementation(libs.test.ktor.client.mock)
            }
        }
        val jvmMain by getting {
            //kotlin.srcDir("$buildDir/generated/sources/templates/kotlin/main")
            dependencies {
                api("com.google.protobuf:protobuf-javalite:3.19.1")
            }
        }
        val androidMain by getting {
            //kotlin.srcDir("$buildDir/generated/sources/templates/kotlin/main")
            dependencies {
                api("com.google.protobuf:protobuf-javalite:3.19.1")
            }
        }
    }
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") { option("lite") }
            }
        }
    }
}

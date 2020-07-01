
import dependency.Library
import dependency.network.AlgoliaClient
import dependency.network.Coroutines
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.test.SL4J
import dependency.test.Mockk
import dependency.ui.*

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("kotlinx-serialization")
    id("maven-publish")
    id("com.jfrog.bintray")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(17)
        targetSdkVersion(30)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    buildTypes {
        getByName("debug") {
            matchingFallbacks = listOf("release")
        }
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        getByName("test") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

group = Library.group
version = Library.version

kotlin {
    metadata {
        mavenPublication {
            artifactId = Library.artifactHelperCommon
        }
    }
    jvm {
        mavenPublication {
            artifactId = Library.artifactHelperJvm
        }
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            }
        }
    }
    android {
        mavenPublication {
            artifactId = Library.artifactHelperAndroid
        }
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":insights"))
                api(AlgoliaClient("common"))
                api(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":insights"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Coroutines("test"))
                implementation(Ktor("client-mock"))
                implementation(AlgoliaClient("common"))
                implementation(Mockk("common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib-jdk8"))
                api(AlgoliaClient("jvm"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(":insights"))
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(SL4J("simple"))
                implementation(AlgoliaClient("jvm"))
                implementation(Ktor("client-mock-jvm"))
                implementation(Mockk())
            }
        }
        val androidMain by getting {
            dependencies {
                api(kotlin("stdlib-jdk8"))
                api(AppCompat())
                api(RecyclerView())
                api(MaterialDesign())
                api(AndroidCore("ktx"))
                api(SwipeRefreshLayout())
                api(Paging())
                api(Coroutines("android"))
                api(AlgoliaClient("android"))
                api(Ktor("client-android"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":insights"))
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(AlgoliaClient("android"))
                implementation(Ktor("client-mock-jvm"))
                implementation(AppCompat())
                implementation(Paging())
                implementation(MaterialDesign())
                implementation(AndroidCore("ktx"))
                implementation(SwipeRefreshLayout())
                implementation(AndroidTestRunner())
                implementation(AndroidTestExt())
                implementation(Robolectric())
            }
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    publish = true
    setPublications("metadata", "jvm", "androidRelease")

    pkg.apply {
        desc = ""
        repo = "maven"
        name = Library.packageName
        websiteUrl = "https://www.algolia.com/"
        issueTrackerUrl =  "https://github.com/algolia/instantsearch-android/issues"
        setLicenses("Apache-2.0")
        setLabels("Kotlin", "Algolia")
        vcsUrl = "https://github.com/algolia/instantsearch-android.git"
        version.apply {
            name = Library.version
            vcsTag = Library.version
        }
    }
}

configurations.create("compileClasspath") //FIXME: Workaround for https://youtrack.jetbrains.com/issue/KT-27170

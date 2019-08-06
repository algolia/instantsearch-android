
import dependency.Library
import dependency.network.AlgoliaClient
import dependency.network.Coroutines
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.test.SL4J
import dependency.ui.*

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("kotlinx-serialization")
    id("maven-publish")
    id("com.jfrog.bintray")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(17)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(AlgoliaClient())
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Ktor("client-mock"))
                implementation(AlgoliaClient())
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api(AlgoliaClient("jvm"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(SL4J("simple"))
                implementation(AlgoliaClient("jvm"))
                implementation(Ktor("client-mock-jvm"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(AppCompat())
                implementation(RecyclerView())
                implementation(MaterialDesign())
                implementation(AndroidCore("ktx"))
                implementation(Paging())
                api(Coroutines("android"))
                api(AlgoliaClient("jvm"))
                api(Ktor("client-android"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(project(":core"))
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(AlgoliaClient("jvm"))
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
        name = Library.artifact
        websiteUrl = "https://www.algolia.com/"
        issueTrackerUrl =  "https://github.com/algolia/instantsearch-kotlin/issues"
        setLicenses("MIT")
        setLabels("Kotlin", "Algolia")
        vcsUrl = "https://github.com/algolia/instantsearch-kotlin.git"
        version.apply {
            name = Library.version
            vcsTag = Library.version
        }
    }
}

configurations.create("compileClasspath") //FIXME: Workaround for https://youtrack.jetbrains.com/issue/KT-27170

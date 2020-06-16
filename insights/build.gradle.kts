import dependency.Library
import dependency.lib.AndroidJob
import dependency.network.AlgoliaClient
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.ui.AndroidCore
import dependency.ui.AppCompat

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("kotlinx-serialization")
    id("maven-publish")
    id("com.jfrog.bintray")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
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

    buildTypes {
        all {
            val env = System.getenv()
            buildConfigField("String", "ALGOLIA_APPLICATION_ID", "\"${env["ALGOLIA_APPLICATION_ID"]}\"")
            buildConfigField("String", "ALGOLIA_API_KEY", "\"${env["ALGOLIA_API_KEY"]}\"")
            buildConfigField("String", "INSIGHTS_VERSION", "\"${Library.version}\"")
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
            artifactId = dependency.Library.artifactInsightsCommon
        }
    }
    jvm {
        mavenPublication {
            artifactId = dependency.Library.artifactInsightsJvm
        }
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    android {
        mavenPublication {
            artifactId = dependency.Library.artifactInsightsAndroid
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
                api(AlgoliaClient("common"))
                api(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Ktor("client-mock"))
                implementation(AlgoliaClient("common"))
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
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(dependency.test.SL4J("simple"))
                implementation(AlgoliaClient("jvm"))
                implementation(Ktor("client-mock-jvm"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(kotlin("stdlib-jdk8"))
                api(AppCompat())
                api(AlgoliaClient("android"))
                api(Ktor("client-android"))
                implementation(AndroidJob())
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(project(":core"))
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(AlgoliaClient("android"))
                implementation(Ktor("client-mock-jvm"))
                implementation(AppCompat())
                implementation(AndroidCore("ktx"))
                implementation(AndroidTestRunner())
                implementation(AndroidTestExt())
                implementation(Robolectric())
            }
        }
    }
}

buildConfigKotlin {
    sourceSet("metadata") {
        buildConfig(name = "INSIGHT_VERSION", value = Library.version)
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
        issueTrackerUrl = "https://github.com/algolia/instantsearch-android/issues"
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

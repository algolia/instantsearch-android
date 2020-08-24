import dependency.network.AlgoliaClient
import dependency.network.Coroutines
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.ui.AndroidCore
import dependency.ui.AppCompat
import dependency.ui.MaterialDesign
import dependency.ui.Paging
import dependency.ui.RecyclerView
import dependency.ui.SwipeRefreshLayout
import java.net.URI

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("kotlinx-serialization")
    id("maven-publish")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
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
    explicitApi()
    android {
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
                api(project(":instantsearch-core"))
                api(AlgoliaClient())
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
        val androidMain by getting {
            dependencies {
                implementation(Coroutines("android"))
                api(AppCompat())
                api(RecyclerView())
                api(MaterialDesign())
                api(AndroidCore("ktx"))
                api(SwipeRefreshLayout())
                api(Paging())
                api(Ktor("client-android"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
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

publishing {
    repositories {
        maven {
            url = URI("https://api.bintray.com/maven/algolia/maven/com.algolia:instantsearch-android/;publish=0")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_KEY")
            }
        }
    }

    publications.withType<MavenPublication>().all {
        groupId = Library.group
        version = Library.version

        val pomArtifactId = project.findMandatoryProperty("POM_ARTIFACT_ID")
        artifactId = when (name) {
            "kotlinMultiplatform" -> pomArtifactId
            else -> "$pomArtifactId-$name"
        }
        pom.name.set(name)
        pom.description.set(description)

        pom.url.set(project.findOptionalProperty("POM_SCM_URL"))
        pom.inceptionYear.set(project.findOptionalProperty("POM_INCEPTION_YEAR"))

        pom.scm {
            url.set(project.findOptionalProperty("POM_SCM_URL"))
            connection.set(project.findOptionalProperty("POM_SCM_CONNECTION"))
            developerConnection.set(project.findOptionalProperty("POM_SCM_DEV_CONNECTION"))
        }

        pom.licenses {
            license {
                name.set(project.findOptionalProperty("POM_LICENCE_NAME"))
                url.set(project.findOptionalProperty("POM_LICENCE_URL"))
                distribution.set(project.findOptionalProperty("POM_LICENCE_DIST"))
            }
        }

        pom.developers {
            developer {
                id.set(project.findOptionalProperty("POM_DEVELOPER_ID"))
                name.set(project.findOptionalProperty("POM_DEVELOPER_NAME"))
                url.set(project.findOptionalProperty("POM_DEVELOPER_URL"))
                email.set(project.findOptionalProperty("POM_DEVELOPER_EMAIL"))
            }
        }
    }
}

fun Project.findMandatoryProperty(propertyName: String): String {
    val value = this.findOptionalProperty(propertyName)
    return requireNotNull(value) { "Please define \"$propertyName\" in your gradle.properties file" }
}

fun Project.findOptionalProperty(propertyName: String) = findProperty(propertyName)?.toString()

configurations.create("compileClasspath") //FIXME: Workaround for https://youtrack.jetbrains.com/issue/KT-27170

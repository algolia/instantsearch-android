import dependency.lib.AndroidJob
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.ui.AndroidCore
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    libraryVariants.all {
        generateBuildConfigProvider.configure { enabled = false }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        //replace after https://youtrack.jetbrains.com/issue/KT-37652
        freeCompilerArgs = freeCompilerArgs + listOf("-Xexplicit-api=warning")
    }

    sourceSets.getByName("main") {
        java.srcDirs("$buildDir/generated/sources/templates/kotlin/main")
    }

    testOptions {
        unitTests {
            it.isIncludeAndroidResources = true
            it.isReturnDefaultValues = true
        }
    }
}

tasks {

    withType<KotlinCompile> {
        dependsOn("copyTemplates")
    }

    register(name = "copyTemplates", type = Copy::class) {
        from("src/main/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("version" to Library.version)
        filteringCharset = "UTF-8"
    }
}

group = Library.group
version = Library.version

dependencies {
    api(project(":instantsearch-android-core"))
    api(AndroidCore("ktx"))
    api(AndroidJob())

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(AndroidTestRunner())
    testImplementation(AndroidTestExt())
    testImplementation(Robolectric())
}

mavenPublish.targets.getByName("uploadArchives") {
    releaseRepositoryUrl = "https://api.bintray.com/maven/algolia/maven/com.algolia:instantsearch-android/;publish=0"
    repositoryUsername = System.getenv("BINTRAY_USER")
    repositoryPassword = System.getenv("BINTRAY_KEY")
}

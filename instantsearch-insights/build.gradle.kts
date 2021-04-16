import dependency.lib.Work
import dependency.network.AlgoliaClient
import dependency.network.Ktor
import dependency.test.AndroidTestExt
import dependency.test.AndroidTestRunner
import dependency.test.Robolectric
import dependency.ui.AndroidCore
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("kotlin-android")
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
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    sourceSets.getByName("main") {
        java.srcDirs("$buildDir/generated/sources/templates/kotlin/main")
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
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

    withType<KotlinCompile> {
        if ("UnitTest" !in name) {
            kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
        }
    }
}

dependencies {
    api(AlgoliaClient())
    implementation(AndroidCore("ktx"))
    implementation(Ktor("client-android"))
    implementation(Work("runtime-ktx"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(AndroidTestRunner())
    testImplementation(AndroidTestExt())
    testImplementation(Robolectric())
    testImplementation(Ktor("client-mock-jvm"))
    testImplementation(Work("testing"))
}

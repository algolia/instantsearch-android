plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
    alias(libs.plugins.dokka)
}

android {
    namespace = "com.algolia.instantsearch.android.paging3"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

// AGP 9 built-in Kotlin: compiler options are configured here instead of
// through the `kotlin-android` plugin.
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=com.algolia.instantsearch.ExperimentalInstantSearch",
                "-opt-in=com.algolia.instantsearch.InternalInstantSearch",
                "-Xexplicit-api=strict"
            )
        )
    }
}

tasks.withType<org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask>().configureEach {
    if (name == "dokkaGeneratePublicationHtml") {
        outputDirectory.set(layout.buildDirectory.dir("intermediates/javadoc/release"))
    }
}

tasks.matching { it.name == "javaDocReleaseGeneration" }.configureEach {
    dependsOn("dokkaGeneratePublicationHtml")
    // Avoid AGP embedded Dokka (ASM9 issue).
    actions.clear()
}

dependencies {
    api(project(":instantsearch"))
    api(libs.androidx.paging3)
    testImplementation(kotlin("test-junit"))
}

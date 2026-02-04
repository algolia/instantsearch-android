plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
    alias(libs.plugins.dokka)
}

android {
    namespace = "com.algolia.instantsearch.android.paging3"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        freeCompilerArgs.addAll(listOf(
            "-Xopt-in=com.algolia.instantsearch.ExperimentalInstantSearch",
            "-Xopt-in=com.algolia.instantsearch.InternalInstantSearch",
            "-Xexplicit-api=strict"
        ))
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

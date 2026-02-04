plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka") version "1.9.20"
}

android {
    namespace = "com.algolia.instantsearch.android.paging3"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.addAll(listOf(
            "-Xopt-in=com.algolia.instantsearch.ExperimentalInstantSearch",
            "-Xopt-in=com.algolia.instantsearch.InternalInstantSearch",
            "-Xexplicit-api=strict"
        ))
    }
}

tasks.matching { it.name == "javaDocReleaseGeneration" }.configureEach {
    dependsOn("dokkaJavadoc")
    // Replace AGP's embedded Dokka execution to avoid ASM9 parsing failures.
    actions.clear()
    doLast {
        val from = layout.buildDirectory.dir("dokka/dokkaJavadoc").get().asFile
        val to = layout.buildDirectory.dir("intermediates/javadoc/release").get().asFile
        if (from.exists()) {
            from.copyRecursively(to, overwrite = true)
        }
    }
}

dependencies {
    api(project(":instantsearch"))
    api(libs.androidx.paging3)
    testImplementation(kotlin("test-junit"))
}

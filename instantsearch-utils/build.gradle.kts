plugins {
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()
    jvm {
        compilations.all { kotlinOptions.jvmTarget = "1.8" }
        testRuns["test"].executionTask.configure { useJUnit() }
    }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
    }
}

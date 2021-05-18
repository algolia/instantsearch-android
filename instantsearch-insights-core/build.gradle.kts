plugins {
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish")
}


kotlin {
    explicitApi()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
        val commonMain by getting {
            dependencies {
                api(dependency.network.AlgoliaClient())
            }
        }
        val commonTest by getting
        val jvmMain by getting
        val jvmTest by getting
    }
}

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("com.algolia.instantsearch.InternalInstantSearch")
                optIn("com.algolia.instantsearch.ExperimentalInstantSearch")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        val commonMain by getting {
            dependencies {
                api(project(":instantsearch-utils"))
                implementation(project(":migration2to3"))
                api(libs.algolia.client)
                implementation(libs.algolia.telemetry)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.atomicfu)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
    }
}

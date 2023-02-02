plugins {
    kotlin("multiplatform")
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
                implementation(libs.algolia.telemetry)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.atomicfu)
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

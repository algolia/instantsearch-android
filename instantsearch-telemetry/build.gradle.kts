plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
}

kotlin {
    explicitApi()
    jvm()
    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
            languageSettings.optIn("com.algolia.instantsearch.Internal")
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":instantsearch-utils"))
                implementation(libs.kotlinx.serialization.protobuf)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
    }
}

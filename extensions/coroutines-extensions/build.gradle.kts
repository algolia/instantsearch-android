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
            }
        }
        val commonMain by getting {
            dependencies {
                api(project(":instantsearch-core"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
                implementation(libs.test.turbine)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
    }
}

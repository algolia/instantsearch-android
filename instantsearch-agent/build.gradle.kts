plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("kotlinx-serialization")
    id("com.vanniktech.maven.publish")
}

// Agent Studio is an EXPERIMENTAL, standalone library. It is versioned
// independently from the rest of InstantSearch (0.x line) via the
// module-local `gradle.properties` `VERSION_NAME` override.
group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

kotlin {
    explicitApi()
    android {
        namespace = "com.algolia.instantsearch.agent"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            // Android target compiles for JVM 11, in line with the other Android
            // artifacts of this repository and current AndroidX bytecode.
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
        // Run the shared `commonTest` suite on the Android host as well.
        withHostTest {}
    }
    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                // The module opts in to its own experimental marker so its
                // internal sources don't have to annotate every call site.
                // Consumers still have to opt in explicitly.
                optIn("com.algolia.instantsearch.agent.ExperimentalAgentStudioApi")
            }
        }

        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines.core)
                api(libs.ktor.client.serialization.json)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
                implementation(libs.test.coroutines)
                implementation(libs.test.ktor.client.mock)
            }
        }
        named("jvmMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        named("androidHostTest") {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        // Explicit-api strictness applies to production code only; test sources
        // (e.g. JUnit `class …Test`) don't need explicit visibility modifiers.
        if (!name.contains("Test")) {
            freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict"))
        }
    }
}

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
                optIn("com.algolia.instantsearch.Internal")
                optIn("com.algolia.instantsearch.ExperimentalInstantSearch")
            }
        }
        val commonMain by getting {
            kotlin.srcDir("$buildDir/generated/sources/templates/kotlin/main")
            dependencies {
                api(project(":instantsearch-utils"))
                implementation(project(":instantsearch-telemetry"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.atomicfu)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin.common)
                implementation(libs.test.kotlin.annotations)
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(libs.test.kotlin.junit)
            }
        }
    }
}

tasks {
    val copyTemplates = register<Copy>("copyTemplates") {
        from("src/commonMain/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        val version = project.extensions.extraProperties["VERSION_NAME"] as String // require clean build
        expand("projectVersion" to version)
        filteringCharset = "UTF-8"
    }

    named<Task>("compileKotlinMetadata") { dependsOn(copyTemplates) }
    named<Task>("compileKotlinJvm") { dependsOn(copyTemplates) }
    named<Task>("sourcesJar") { dependsOn(copyTemplates) }
}

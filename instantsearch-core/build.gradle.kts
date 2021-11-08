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
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("com.algolia.instantsearch.ExperimentalInstantSearch")
        }
        val commonMain by getting {
            kotlin.srcDir("$buildDir/generated/sources/templates/kotlin/main")
            dependencies {
                implementation(project(":instantsearch-utils"))
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

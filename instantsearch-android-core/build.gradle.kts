import dependency.network.Coroutines
import dependency.util.AtomicFu

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
        val commonMain by getting {
            kotlin.srcDir("$buildDir/generated/sources/templates/kotlin/main")
            dependencies {
                implementation(Coroutines("core"))
                implementation(AtomicFu())
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    val copyTemplates = register<Copy>("copyTemplates") {
        from("src/commonMain/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("projectVersion" to Library.version)
        filteringCharset = "UTF-8"
    }

    named<Task>("compileKotlinMetadata") { dependsOn(copyTemplates) }
    named<Task>("compileKotlinJvm") { dependsOn(copyTemplates) }
    named<Task>("sourcesJar") { dependsOn(copyTemplates) }
}

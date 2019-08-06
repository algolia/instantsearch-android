
import dependency.Library
import dependency.network.Coroutines
import dependency.script.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray")
    id("com.github.ben-manes.versions") version "0.21.0"
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

buildConfigKotlin {
    sourceSet("metadata") {
        buildConfig(name = "version", value = Library.version)
    }
}

group = Library.group
version = Library.version

kotlin {
    metadata {
        mavenPublication {
            artifactId = Library.artifactCoreCommon
        }
    }
    jvm {
        mavenPublication {
            artifactId = Library.artifactCoreJvm
        }
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            kotlin.srcDirs("build/generated/source")
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Coroutines("core-common"))
                implementation(AtomicFu("common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(Coroutines("core"))
                implementation(AtomicFu("jvm"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    publish = true
    setPublications("metadata", "jvm")

    pkg.apply {
        desc = ""
        repo = "maven"
        name = Library.artifact
        websiteUrl = "https://www.algolia.com/"
        issueTrackerUrl =  "https://github.com/algolia/instantsearch-kotlin/issues"
        setLicenses("MIT")
        setLabels("Kotlin", "Algolia")
        vcsUrl = "https://github.com/algolia/instantsearch-kotlin.git"
        version.apply {
            name = Library.version
            vcsTag = Library.version
        }
    }
}

tasks {
    withType<KotlinCompile> {
        dependsOn("generateMetadataBuildConfigKotlin")
    }
}

configurations.create("compileClasspath") //FIXME: Workaround for https://youtrack.jetbrains.com/issue/KT-27170
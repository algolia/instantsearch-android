import dependency.network.Coroutines
import dependency.script.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray")
}

group = Library.group
version = Library.version

kotlin {
    explicitApi()
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
            kotlin.srcDirs("$buildDir/generated/sources/templates/kotlin/main")
            dependencies {
                api(kotlin("stdlib-common"))
                api(Coroutines("core"))
                implementation(AtomicFu()) // "TODO: "common" to be fixed in atomicfu next release
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
                api(kotlin("stdlib-jdk8"))
                api(Coroutines("core-jvm"))
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
        name = Library.packageName
        websiteUrl = "https://www.algolia.com/"
        issueTrackerUrl = "https://github.com/algolia/instantsearch-android/issues"
        setLicenses("Apache-2.0")
        setLabels("Kotlin", "Algolia")
        vcsUrl = "https://github.com/algolia/instantsearch-android.git"
        version.apply {
            name = Library.version
            vcsTag = Library.version
        }
    }
}

tasks {
    withType<KotlinCompile> {
        dependsOn("copyTemplates")
    }

    register(name = "copyTemplates", type = Copy::class) {
        from("src/commonMain/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("projectVersion" to Library.version)
        filteringCharset = "UTF-8"
    }
}

configurations.create("compileClasspath") //FIXME: Workaround for https://youtrack.jetbrains.com/issue/KT-27170

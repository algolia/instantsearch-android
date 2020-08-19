import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import dependency.network.Coroutines
import dependency.script.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

plugins {
    id("kotlin-multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray")
}

group = Library.group
version = Library.version

kotlin {
    explicitApi()
    jvm {
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
                api(Coroutines())
                implementation(AtomicFu())
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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

tasks {
    withType<KotlinCompileCommon> {
        dependsOn("copyTemplates")
    }

    register(name = "copyTemplates", type = Copy::class) {
        from("src/commonMain/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("projectVersion" to Library.version)
        filteringCharset = "UTF-8"
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    publish = true

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

// Workaround until Bintray gradle plugin caches up:
// https://github.com/bintray/gradle-bintray-plugin/issues/229#issuecomment-473123891
tasks.withType<BintrayUploadTask> {
    doFirst {
        publishing.publications
            .filterIsInstance<MavenPublication>()
            .forEach { publication ->
                val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                if (moduleFile.exists()) {
                    publication.artifact(object :
                        org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(moduleFile) {
                        override fun getDefaultExtension() = "module"
                    })
                }
            }
    }
}

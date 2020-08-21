import dependency.network.Coroutines
import dependency.script.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import publish.MavenPublishing
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

plugins {
    id("kotlin-multiplatform")
    id("maven-publish")
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
                api(Coroutines("core"))
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
    withType<KotlinCompile> {
        dependsOn("copyTemplates")
    }

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

publishing {
    MavenPublishing.configurePublish(this, project)
}

import dependency.network.Coroutines
import dependency.script.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon
import java.net.URI

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
    repositories {
        maven {
            url = URI("https://api.bintray.com/maven/algolia/maven/com.algolia:instantsearch-android/;publish=0")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_KEY")
            }
        }
    }

    publications.withType<MavenPublication>().all {
        groupId = Library.group
        version = Library.version

        val pomArtifactId = project.findMandatoryProperty("POM_ARTIFACT_ID")
        artifactId = when (name) {
            "kotlinMultiplatform" -> pomArtifactId
            else -> "$pomArtifactId-$name"
        }
        pom.name.set(name)
        pom.description.set(description)

        pom.url.set(project.findOptionalProperty("POM_SCM_URL"))
        pom.inceptionYear.set(project.findOptionalProperty("POM_INCEPTION_YEAR"))

        pom.scm {
            url.set(project.findOptionalProperty("POM_SCM_URL"))
            connection.set(project.findOptionalProperty("POM_SCM_CONNECTION"))
            developerConnection.set(project.findOptionalProperty("POM_SCM_DEV_CONNECTION"))
        }

        pom.licenses {
            license {
                name.set(project.findOptionalProperty("POM_LICENCE_NAME"))
                url.set(project.findOptionalProperty("POM_LICENCE_URL"))
                distribution.set(project.findOptionalProperty("POM_LICENCE_DIST"))
            }
        }

        pom.developers {
            developer {
                id.set(project.findOptionalProperty("POM_DEVELOPER_ID"))
                name.set(project.findOptionalProperty("POM_DEVELOPER_NAME"))
                url.set(project.findOptionalProperty("POM_DEVELOPER_URL"))
                email.set(project.findOptionalProperty("POM_DEVELOPER_EMAIL"))
            }
        }
    }
}

fun Project.findMandatoryProperty(propertyName: String): String {
    val value = this.findOptionalProperty(propertyName)
    return requireNotNull(value) { "Please define \"$propertyName\" in your gradle.properties file" }
}

fun Project.findOptionalProperty(propertyName: String) = findProperty(propertyName)?.toString()

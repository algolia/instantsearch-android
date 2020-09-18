
import java.net.URI

plugins {
    id("maven-publish")
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.10"))
        classpath(kotlin("serialization",  version = "1.4.10"))
        classpath(dependency.plugin.AndroidTools())
        classpath(dependency.plugin.GradleMavenPublish())
    }
}

project.extensions.extraProperties.set("GROUP", Library.group)
project.extensions.extraProperties.set("VERSION_NAME", Library.version)

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven { url = URI("https://dl.bintray.com/algolia/maven") }
        maven { url = URI("https://kotlin.bintray.com/kotlinx") }
        maven { url = URI("https://dl.bintray.com/kotlin/ktor") }
    }
}

tasks.withType<Test> {
    maxParallelForks = Runtime.getRuntime().availableProcessors().minus(1).coerceAtLeast(1)
}

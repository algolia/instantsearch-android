
import java.net.URI

plugins {
    id("maven-publish")
    id("com.github.ben-manes.versions") version "0.27.0"
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(dependency.script.AndroidTools())
        classpath(kotlin("gradle-plugin", version = "1.4.0"))
        classpath(kotlin("serialization",  version = "1.4.0"))
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

allprojects {
    repositories {
        mavenLocal()
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

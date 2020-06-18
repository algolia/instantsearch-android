import java.net.URI

plugins {
    id("java-library-convention")
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
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
        classpath(kotlin("serialization", version = "1.3.72"))
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = URI("https://dl.bintray.com/algolia/maven") }
        maven { url = URI("https://kotlin.bintray.com/kotlinx") }
        maven { url = URI("https://dl.bintray.com/kotlin/ktor") }
        maven { url = URI("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

tasks.withType<Test> {
    maxParallelForks = Runtime.getRuntime().availableProcessors().minus(1).coerceAtLeast(1)
}

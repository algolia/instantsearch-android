
import java.net.URI

plugins {
    id("java-library-convention")
    id("maven-publish")
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(dependency.script.AndroidTools())
        classpath(kotlin("gradle-plugin", version = "1.3.41"))
        classpath(kotlin("serialization",  version = "1.3.41"))
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven { url = URI("https://dl.bintray.com/algolia/maven") }
        maven { url = URI("https://kotlin.bintray.com/kotlinx") }
        maven { url = URI("https://dl.bintray.com/kotlin/ktor") }
    }
}
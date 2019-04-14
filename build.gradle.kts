import java.net.URI

plugins {
    id("java-library-convention")
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies{
        classpath(AndroidTools())
        classpath(kotlin("gradle-plugin", version = "1.3.30"))
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven { url = URI("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = URI("https://kotlin.bintray.com/kotlinx") }
        maven { url = URI("https://dl.bintray.com/kotlin/ktor") }
    }
}
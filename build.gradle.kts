import com.diffplug.gradle.spotless.SpotlessExtension

buildscript {
    val kotlinVersion by extra("1.5.10")
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.17.0")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:5.14.0")
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            ktlint("0.41.0")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}

tasks.withType<Test> {
    maxParallelForks = Runtime.getRuntime().availableProcessors().minus(1).coerceAtLeast(1)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

import com.diffplug.gradle.spotless.SpotlessExtension

buildscript {
    val kotlinVersion by extra("1.4.32")
    repositories {
        mavenCentral()
        google()
        jcenter { //TODO: remove when the dependencies below are migrated
            content {
                includeGroup("org.jetbrains.dokka")
                includeModule("org.jetbrains.trove4j", "trove4j")
                includeModule("org.jetbrains", "markdown")
            }
        }
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath(dependency.plugin.AndroidTools())
        classpath(dependency.plugin.GradleMavenPublish())
        classpath(dependency.plugin.Spotless())
        classpath(dependency.plugin.Dokka())
    }
}

project.extensions.extraProperties.apply {
    set("GROUP", Library.group)
    set("VERSION_NAME", Library.version)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    repositories {
        mavenCentral()
        google()
        jcenter { //TODO: remove when the dependencies below are migrated
            content {
                includeGroup("org.jetbrains.dokka")
                includeModule("org.jetbrains.trove4j", "trove4j")
                includeModule("org.jetbrains", "markdown")
                includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
                includeModule("org.jetbrains.kotlinx", "kotlinx-collections-immutable-jvm")
            }
        }
    }
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            ktlint("0.39.0")
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

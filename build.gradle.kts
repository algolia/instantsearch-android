import com.diffplug.gradle.spotless.SpotlessExtension

buildscript {
    val kotlinVersion by extra("1.5.31")
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:5.15.0")
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
            ktlint("0.43.0")
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

tasks.register("runDebugUnitTest") {
    dependsOn(":instantsearch-core:jvmTest")
    dependsOn(":instantsearch:testDebugUnitTest")
    dependsOn(":instantsearch-insights:testDebugUnitTest")
    dependsOn(":instantsearch-compose:testDebugUnitTest")
}

import com.diffplug.gradle.spotless.SpotlessExtension

buildscript {
    val kotlinVersion by extra("1.6.10")
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.19.0")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.4.1")
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    repositories {
        mavenCentral()
        google()
    }
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
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
    dependsOn(":extensions:android-paging3:testDebugUnitTest")
    dependsOn(":extensions:android-loading:testDebugUnitTest")
}

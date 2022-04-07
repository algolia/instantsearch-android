import com.diffplug.gradle.spotless.SpotlessExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplaform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
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

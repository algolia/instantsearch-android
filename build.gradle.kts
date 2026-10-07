import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.kotlin.multiplaform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.dokka) apply false
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
    delete(rootProject.layout.buildDirectory)
}

tasks.register("runDebugUnitTest") {
    dependsOn(":instantsearch-core:jvmTest")
    dependsOn(":instantsearch:testAndroidHostTest")
    dependsOn(":instantsearch-insights:testAndroidHostTest")
    dependsOn(":instantsearch-compose:testDebugUnitTest")
    dependsOn(":instantsearch-agent:jvmTest")
    dependsOn(":extensions:android-paging3:testDebugUnitTest")
    dependsOn(":extensions:android-loading:testDebugUnitTest")
    dependsOn(":extensions:coroutines-extensions:jvmTest")
}

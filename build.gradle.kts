import com.diffplug.gradle.spotless.SpotlessExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplaform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.android) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.kotlin.android) apply false
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

    plugins.withId("com.android.library") {
        // Use Dokka plugin instead of AGP embedded Dokka (ASM9 issue).
        pluginManager.apply("org.jetbrains.dokka")

        tasks.matching { it.name == "javaDocReleaseGeneration" }.configureEach {
            dependsOn("dokkaJavadoc")
            actions.clear()
            doLast {
                val from = layout.buildDirectory.dir("dokka/dokkaJavadoc").get().asFile
                val to = layout.buildDirectory.dir("intermediates/javadoc/release").get().asFile
                if (from.exists()) {
                    from.copyRecursively(to, overwrite = true)
                }
            }
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
    dependsOn(":extensions:coroutines-extensions:jvmTest")
}

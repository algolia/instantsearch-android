plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()
    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }
    android {
        namespace = "com.algolia.instantsearch.utils"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            // Current AndroidX artifacts (core, appcompat, work, ...) ship Java 11
            // bytecode, so the Android target must be compiled for JVM 11 too.
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    sourceSets {
        all {
            languageSettings.optIn("com.algolia.instantsearch.InternalInstantSearch")
        }

        val commonMain by getting

        val commonJvm by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(commonJvm)
        }

        val jvmMain by getting {
            dependsOn(commonJvm)
        }
    }
}

tasks {
    val copyTemplates = register<Copy>("copyTemplates") {
        from("src/commonMain/templates")
        into(layout.buildDirectory.dir("generated/sources/templates/kotlin/main"))
        val version = project.extensions.extraProperties["VERSION_NAME"] as String // require clean build
        expand("projectVersion" to version)
        filteringCharset = "UTF-8"
    }
    kotlin.sourceSets.commonMain.get().kotlin.srcDir(copyTemplates)
}

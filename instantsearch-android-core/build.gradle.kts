import dependency.network.Coroutines
import dependency.util.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

plugins {
    kotlin("multiplatform")
    id ("com.vanniktech.maven.publish")
}

kotlin {
    explicitApi()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
        val commonMain by getting {
            dependencies {
                implementation(Coroutines("core"))
                implementation(AtomicFu())
            }
            kotlin.srcDir("$buildDir/generated/sources/templates/kotlin/main")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    named<KotlinCompileCommon>("compileKotlinMetadata") {
        dependsOn("copyTemplates")
    }

    register(name = "copyTemplates", type = Copy::class) {
        from("src/commonMain/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("projectVersion" to Library.version)
        filteringCharset = "UTF-8"
    }
}

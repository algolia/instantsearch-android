import dependency.network.Coroutines
import dependency.util.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("java-library")
    id ("com.vanniktech.maven.publish")
}

sourceSets {
    main {
        java.srcDirs("$buildDir/generated/sources/templates/kotlin/main")
    }
}

dependencies {
    implementation(Coroutines("core"))
    implementation(AtomicFu())
    testImplementation(kotlin("test-junit"))
}

tasks {
    named<KotlinCompile>("compileKotlin") {
        dependsOn("copyTemplates")
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += listOf(
                "-Xexplicit-api=strict",
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }

    register(name = "copyTemplates", type = Copy::class) {
        from("src/main/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("projectVersion" to Library.version)
        filteringCharset = "UTF-8"
    }

    named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

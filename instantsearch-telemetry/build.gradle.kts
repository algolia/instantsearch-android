import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.8.17"
}

dependencies {
    api("com.google.protobuf:protobuf-kotlin-lite:3.19.1")
    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                getByName("java") { option("lite") }
                id("kotlin")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

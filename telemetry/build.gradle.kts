import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.daemon.client.KotlinCompilerClient.compile

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.8.17"
}

dependencies {
    implementation("com.google.protobuf:protobuf-kotlin:3.19.1")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("kotlin")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

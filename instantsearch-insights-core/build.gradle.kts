import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
}

dependencies {
    api(dependency.network.AlgoliaClient())
    //testImplementation(kotlin("test-junit"))
    //testImplementation(kotlin("test-annotations-common"))
    //testImplementation(dependency.network.Ktor("client-mock-jvm"))
    //testImplementation(Coroutines("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-Xexplicit-api=strict",
            "-Xopt-in=kotlin.RequiresOptIn",
        )
    }
}

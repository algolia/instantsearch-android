import dependency.network.Coroutines
import dependency.script.AtomicFu
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

plugins {
    kotlin("jvm")
    id("java-library")
    id("com.vanniktech.maven.publish")
}

group = Library.group
version = Library.version

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
    withType<KotlinCompile> {
        dependsOn("copyTemplates")
    }

    withType<KotlinCompileCommon> {
        dependsOn("copyTemplates")
    }

    register(name = "copyTemplates", type = Copy::class) {
        from("src/main/templates")
        into("$buildDir/generated/sources/templates/kotlin/main")
        expand("projectVersion" to Library.version)
        filteringCharset = "UTF-8"
    }
}

mavenPublish.targets.getByName("uploadArchives") {
    releaseRepositoryUrl = "https://api.bintray.com/maven/algolia/maven/com.algolia:instantsearch-android/;publish=0"
    repositoryUsername = System.getenv("BINTRAY_USER")
    repositoryPassword = System.getenv("BINTRAY_KEY")
}

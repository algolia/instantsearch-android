import dependency.network.Coroutines
import dependency.script.AtomicFu

plugins {
    id("kotlin-multiplatform")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Coroutines("core-common"))
                implementation(AtomicFu("common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(Coroutines("core"))
                implementation(AtomicFu())
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

configurations.create("compileClasspath") //FIXME: Workaround for https://youtrack.jetbrains.com/issue/KT-27170
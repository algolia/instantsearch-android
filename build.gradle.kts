import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies.classpath(AndroidTools())
}

allprojects {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven { url = URI("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = URI("https://kotlin.bintray.com/kotlinx") }
        maven { url = URI("https://dl.bintray.com/kotlin/ktor") }
    }
}
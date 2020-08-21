package publish

import org.gradle.api.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.kotlin.dsl.*
import java.net.URI

object MavenPublishing {

    fun configurePublish(publishingExtension: PublishingExtension, project: Project) {
        publishingExtension.repositories {
            maven {
                url = URI("https://api.bintray.com/maven/algolia/maven/com.algolia:instantsearch-android/;publish=0")
                credentials {
                    username = System.getenv("BINTRAY_USER")
                    password = System.getenv("BINTRAY_KEY")
                }
            }
        }

        publishingExtension.publications.withType<MavenPublication>().all {
            val mavenPublishPom = MavenPublishPom(project)
            groupId = Library.group
            version = Library.version
            artifactId = when (name) {
                "kotlinMultiplatform" -> mavenPublishPom.artifactId
                else -> "${mavenPublishPom.artifactId}-$name"
            }
            mavenPublishPom.configurePom(pom)
        }
    }
}

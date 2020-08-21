package publish

import Library
import org.gradle.api.*
import org.gradle.api.publish.maven.*

class MavenPublishPom(
    project: Project
) {
    val groupId = Library.group
    val artifactId = project.findMandatoryProperty("POM_ARTIFACT_ID")
    val version = Library.version

    val name = project.findOptionalProperty("POM_NAME")
    val packaging = project.findOptionalProperty("POM_PACKAGING")
    val description = project.findOptionalProperty("POM_DESCRIPTION")
    val url = project.findOptionalProperty("POM_URL")
    val inceptionYear = project.findOptionalProperty("POM_INCEPTION_YEAR")

    val scmUrl = project.findOptionalProperty("POM_SCM_URL")
    val scmConnection = project.findOptionalProperty("POM_SCM_CONNECTION")
    val scmDeveloperConnection = project.findOptionalProperty("POM_SCM_DEV_CONNECTION")

    val licenseName = project.findOptionalProperty("POM_LICENCE_NAME")
    val licenseUrl = project.findOptionalProperty("POM_LICENCE_URL")
    val licenseDistribution = project.findOptionalProperty("POM_LICENCE_DIST")

    val developerId = project.findOptionalProperty("POM_DEVELOPER_ID")
    val developerName = project.findOptionalProperty("POM_DEVELOPER_NAME")
    val developerUrl = project.findOptionalProperty("POM_DEVELOPER_URL")
    val developerEmail = project.findOptionalProperty("POM_DEVELOPER_EMAIL")

    fun configurePom(pom: MavenPom) {
        pom.name.set(name)
        pom.description.set(description)
        pom.url.set(url)
        pom.inceptionYear.set(inceptionYear)

        pom.scm {
            url.set(scmUrl)
            connection.set(scmConnection)
            developerConnection.set(scmDeveloperConnection)
        }

        pom.licenses {
            license {
                name.set(licenseName)
                url.set(licenseUrl)
                distribution.set(licenseDistribution)
            }
        }

        pom.developers {
            developer {
                id.set(developerId)
                name.set(developerName)
                url.set(developerUrl)
                email.set(developerEmail)
            }
        }
    }

    internal fun Project.findMandatoryProperty(propertyName: String): String {
        val value = this.findOptionalProperty(propertyName)
        return requireNotNull(value) { "Please define \"$propertyName\" in your gradle.properties file" }
    }

    internal fun Project.findOptionalProperty(propertyName: String) = findProperty(propertyName)?.toString()
}

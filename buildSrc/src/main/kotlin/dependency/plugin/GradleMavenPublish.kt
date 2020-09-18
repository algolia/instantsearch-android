package dependency.plugin

import dependency.Dependency

object GradleMavenPublish : Dependency {

    override val group = "com.vanniktech"
    override val artifact = "gradle-maven-publish-plugin"
    override val version = "0.12.0"
}

package dependency.plugin

import dependency.Dependency

object Dokka : Dependency {

    override val group = "org.jetbrains.dokka"
    override val artifact = "dokka-gradle-plugin"
    override val version = "1.4.20"
}

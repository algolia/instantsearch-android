package dependency.plugin

import dependency.Dependency

object Spotless : Dependency {

    override val group = "com.diffplug.spotless"
    override val artifact = "spotless-plugin-gradle"
    override val version = "5.5.2"
}

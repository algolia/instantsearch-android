package dependency.plugin

import dependency.Dependency

object AndroidTools : Dependency {

    override val group = "com.android.tools.build"
    override val artifact = "gradle"
    override val version = "7.0.0-beta03"
}

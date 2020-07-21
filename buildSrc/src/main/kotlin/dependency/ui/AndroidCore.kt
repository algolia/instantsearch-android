package dependency.ui

import dependency.Dependency

object AndroidCore : Dependency {

    override val group: String = "androidx.core"
    override val artifact: String = "core"
    override val version: String = "1.3.0"
}

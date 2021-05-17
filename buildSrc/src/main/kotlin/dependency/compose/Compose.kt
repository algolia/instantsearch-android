package dependency.compose

import dependency.Dependency

interface Compose : Dependency {

    override val version: String
        get() = "1.0.0-beta06"
}

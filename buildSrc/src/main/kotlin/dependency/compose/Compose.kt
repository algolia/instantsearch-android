package dependency.compose

import dependency.Dependency

interface Compose : Dependency {

    override val version: String
        get() = Compose.version

    companion object {
        const val version = "1.0.0-beta09"
    }
}

package dependency.network

import dependency.Dependency

object Ktor : Dependency {

    override val group = "io.ktor"
    override val artifact = "ktor"
    override val version = "1.2.0-alpha-1.3.0-eap-125" // TODO 1.3.30
}
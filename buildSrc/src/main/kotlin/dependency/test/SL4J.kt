package dependency.test

import dependency.Dependency

object SL4J : Dependency {

    override val group = "org.slf4j"
    override val artifact = "slf4j"
    override val version = "1.7.30"
}

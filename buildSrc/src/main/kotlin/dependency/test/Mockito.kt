package dependency.test

import dependency.Dependency


object Mockito : Dependency {

    override val group = "org.mockito"
    override val artifact = "mockito"
    override val version = "2.28.2"
}
package dependency.test

import dependency.Dependency

object Mockk : Dependency {

    override val group = "io.mockk"
    override val artifact = "mockk"
    override val version = "1.10.0"
}

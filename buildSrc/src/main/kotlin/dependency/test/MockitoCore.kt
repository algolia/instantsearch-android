package dependency.test

import dependency.Dependency


object MockitoCore : Dependency {

    override val group = "org.mockito"
    override val artifact = "mockito-core"
    override val version = versionMockito
}
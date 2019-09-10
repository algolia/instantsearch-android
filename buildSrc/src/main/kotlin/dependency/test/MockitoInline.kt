package dependency.test

import dependency.Dependency

object MockitoInline : Dependency {

    override val group = "org.mockito"
    override val artifact = "mockito-inline"
    override val version = versionMockito
}
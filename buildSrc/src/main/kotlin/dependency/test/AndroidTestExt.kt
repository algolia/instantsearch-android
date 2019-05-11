package dependency.test

import dependency.Dependency


object AndroidTestExt : Dependency {

    override val group = "androidx.test.ext"
    override val artifact = "junit"
    override val version = "1.1.0"
}
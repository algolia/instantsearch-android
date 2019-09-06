package dependency.test

import dependency.Dependency

object AndroidTestRunner: Dependency {

    override val group = "androidx.test"
    override val artifact = "runner"
    override val version = "1.2.0"
}
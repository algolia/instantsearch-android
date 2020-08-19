package dependency.test

import dependency.Dependency

object Espresso: Dependency {

    override val group = "androidx.test.espresso"
    override val artifact = "espresso"
    override val version = "3.2.0"
}

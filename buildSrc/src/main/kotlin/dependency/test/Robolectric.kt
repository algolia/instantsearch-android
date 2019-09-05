package dependency.test

import dependency.Dependency


object Robolectric : Dependency {

    override val group = "org.robolectric"
    override val artifact = "robolectric"
    override val version = "4.3"
}
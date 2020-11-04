package dependency.async

import dependency.Dependency

object LiveData : Dependency {

    override val group = "androidx.lifecycle"
    override val artifact = "lifecycle-livedata-ktx"
    override val version = "2.2.0"
}

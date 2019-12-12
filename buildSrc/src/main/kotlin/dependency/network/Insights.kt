package dependency.network

import dependency.Dependency


object Insights : Dependency {

    override val group: String = "com.algolia.instantsearch-android"
    override val artifact: String = "insights"
    override val version: String = "3.0.0"
}
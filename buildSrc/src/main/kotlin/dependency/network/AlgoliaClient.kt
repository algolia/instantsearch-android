package dependency.network

import dependency.Dependency


object AlgoliaClient : Dependency {

    override val group = "com.algolia"
    override val artifact = "algoliasearch-client-kotlin"
    override val version = "1.5.0-beta01"
}

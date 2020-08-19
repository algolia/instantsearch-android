package dependency.network

import dependency.Dependency


object AlgoliaClient : Dependency {

    override val group = "com.algolia"
    override val artifact = "algoliasearch-client-kotlin"
    override val version = "2.0.0-alpha01"
}

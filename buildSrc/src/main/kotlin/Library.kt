import dependency.Dependency

object Library: Dependency {

    override val group = "com.algolia"
    override val artifact = "instantsearch"
    override val version = "2.6.0-alpha01"

    val androidArtifact = "$artifact-android"
    val packageName = "$group:$androidArtifact"
}

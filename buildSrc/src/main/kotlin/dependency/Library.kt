package dependency


object Library: Dependency  {

    override val group = "com.algolia"
    override val artifact = "instantsearch-kotlin"
    override val version = "1.0.0-beta01"

    val artifactCore = "$artifact-core"
    val artifactCoreCommon = "$artifactCore-common"
    val artifactCoreJvm = "$artifactCore-jvm"

    val artifactHelper = "$artifact-helper"
    val artifactHelperCommon = "$artifactHelper-common"
    val artifactHelperJvm = "$artifactHelper-jvm"
    val artifactHelperAndroid = "$artifactHelper-android"
}
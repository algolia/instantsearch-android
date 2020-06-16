package dependency

object Library : Dependency {

    override val group = "com.algolia"
    override val artifact = "instantsearch"
    override val version = "2.5.0-alpha1"

    val packageName = "$group:$artifact-android"

    val artifactCore = "$artifact-core"
    val artifactCoreCommon = "$artifactCore-common"
    val artifactCoreJvm = "$artifactCore-jvm"

    val artifactHelper = "$artifact-helper"
    val artifactHelperCommon = "$artifactHelper-common"
    val artifactHelperJvm = "$artifactHelper-jvm"
    val artifactHelperAndroid = "$artifact-android"

    val artifactInsights = "$artifact-insights"
    val artifactInsightsCommon = "$artifactInsights-common"
    val artifactInsightsJvm = "$artifactInsights-jvm"
    val artifactInsightsAndroid = "$artifactInsights-android"
}

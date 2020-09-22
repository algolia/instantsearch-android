package com.algolia.instantsearch.insights.internal.webservice

import android.os.Build
import com.algolia.instantsearch.insights.BuildConfig

/**
 * Computing the User Agent. Expected output: insights-android (2.1.0); Android (12.1.0)
 */
internal fun computeUserAgent(): String {

    data class LibraryVersion(val name: String, val version: String)

    val insightsVersion = LibraryVersion(
        name = "insights-android",
        version = BuildConfig.INSIGHTS_VERSION
    )
    val androidVersion = LibraryVersion(
        name = "Android",
        version = "${Build.VERSION.SDK_INT}.0.0"
    )
    val headers = arrayOf(insightsVersion, androidVersion)

    return headers.joinToString(separator = "; ") {
        "${it.name} (${it.version})"
    }
}

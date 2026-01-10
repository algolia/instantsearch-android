package com.algolia.instantsearch.migration2to3


public data class RetryableHost(
    val url: String,
    val callType: CallType? = null
) {

    internal var isUp: Boolean = true
    internal var lastUpdated: Long = Time.getCurrentTimeMillis()
    internal var retryCount: Int = 0
}

package com.algolia.instantsearch.migration2to3

import kotlin.collections.plusAssign
import kotlin.compareTo

internal val ApplicationID.searchHosts
    get() = listOf(
        RetryableHost("$this-dsn.algolia.net", CallType.Read),
        RetryableHost("$this.algolia.net", CallType.Write),
    ) + mutableListOf(
        RetryableHost("$this-1.algolianet.com"),
        RetryableHost("$this-2.algolianet.com"),
        RetryableHost("$this-3.algolianet.com")
    ).apply { shuffle() }

internal val insightHosts = listOf(RetryableHost("insights.algolia.io"))
//internal val Region.Analytics.hosts get() = listOf(RetryableHost("analytics.$this.algolia.com"))
//internal val Region.Personalization.hosts get() = listOf(RetryableHost("personalization.$this.algolia.com"))
//internal val placesHosts = listOf(
//    RetryableHost("places-dsn.algolia.net"),
//) + mutableListOf(
//    RetryableHost("places-1.algolianet.com"),
//    RetryableHost("places-2.algolianet.com"),
//    RetryableHost("places-3.algolianet.com")
//).apply { shuffle() }

@PublishedApi
internal fun RetryableHost.reset() {
    lastUpdated = Time.getCurrentTimeMillis()
    isUp = true
    retryCount = 0
}

internal fun RetryableHost.hasTimedOut() {
    isUp = true
    lastUpdated = Time.getCurrentTimeMillis()
    retryCount += 1
}

internal fun RetryableHost.hasFailed() {
    isUp = false
    lastUpdated = Time.getCurrentTimeMillis()
}

internal fun List<RetryableHost>.expireHostsOlderThan(hostStatusExpirationDelayMS: Long) {
    forEach {
        val timeDelayExpired = Time.getCurrentTimeMillis() - it.lastUpdated
        if (timeDelayExpired > hostStatusExpirationDelayMS) {
            it.reset()
        }
    }
}

internal fun List<RetryableHost>.filterCallType(callType: CallType): List<RetryableHost> {
    return filter { it.callType == callType || it.callType == null }
}

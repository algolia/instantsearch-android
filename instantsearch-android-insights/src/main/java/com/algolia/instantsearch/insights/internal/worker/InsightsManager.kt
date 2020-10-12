package com.algolia.instantsearch.insights.internal.worker

internal interface InsightsManager {

    fun setInterval(intervalInMinutes: Long) = Unit

    fun startPeriodicUpload()

    fun startOneTimeUpload()
}

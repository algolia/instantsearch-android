package com.algolia.instantsearch.insights.internal.worker

internal interface InsightsWorker {

    fun setInterval(intervalInMinutes: Long) = Unit

    fun startPeriodicUpload()

    fun startOneTimeUpload()
}

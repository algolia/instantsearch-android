package com.algolia.instantsearch.insights.event

internal interface EventUploader {

    fun setInterval(intervalInMinutes: Long)

    fun startPeriodicUpload()

    fun startOneTimeUpload()
}

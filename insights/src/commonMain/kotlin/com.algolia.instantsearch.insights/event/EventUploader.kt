package com.algolia.instantsearch.insights.event

internal interface EventUploader {

    fun setInterval(intervalInMinutes: Long) = Unit

    fun startPeriodicUpload()

    fun startOneTimeUpload()
}

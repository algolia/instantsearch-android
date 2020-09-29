package com.algolia.instantsearch.insights.internal.event

internal interface EventUploader {

    fun setInterval(intervalInMinutes: Long) = Unit

    fun startPeriodicUpload()

    fun startOneTimeUpload()
}

package com.algolia.instantsearch.insights.internal.uploader

import com.algolia.instantsearch.insights.internal.event.EventResponse

internal interface InsightsUploader {
    val applicationID: String
    val apiKey: String

    suspend fun uploadAll(): List<EventResponse>
}

package com.algolia.instantsearch.insights.internal.uploader

import com.algolia.instantsearch.insights.internal.event.EventResponse

internal interface InsightsUploader {

    suspend fun uploadAll(): List<EventResponse>
}

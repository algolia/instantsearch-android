package com.algolia.instantsearch.insights.internal.uploader

import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.search.configuration.Credentials

internal interface InsightsUploader : Credentials {

    suspend fun uploadAll(): List<EventResponse>
}

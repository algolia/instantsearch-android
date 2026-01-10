package com.algolia.instantsearch.insights.internal.data.distant

import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.migration2to3.Credentials
import com.algolia.instantsearch.migration2to3.InsightsEvent


internal interface InsightsDistantRepository : Credentials {

    suspend fun send(event: InsightsEvent): EventResponse
}

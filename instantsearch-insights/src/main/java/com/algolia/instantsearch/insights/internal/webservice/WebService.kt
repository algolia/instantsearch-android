package com.algolia.instantsearch.insights.internal.webservice

import com.algolia.search.model.insights.InsightsEvent
import io.ktor.client.statement.HttpResponse

internal interface WebService {

    data class Response(
        val errorMessage: String?,
        val code: Int,
    )

    suspend fun send(vararg events: InsightsEvent): HttpResponse
}

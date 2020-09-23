package com.algolia.instantsearch.insights.internal.webservice

import com.algolia.instantsearch.insights.internal.event.EventInternal

internal interface WebService {

    data class Response(
        val errorMessage: String?,
        val code: Int,
    )

    fun send(vararg event: EventInternal): Response
}

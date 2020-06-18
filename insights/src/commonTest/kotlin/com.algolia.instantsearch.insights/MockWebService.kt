package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.instantsearch.insights.internal.webservice.WebService

internal class MockWebService : WebService {

    var code: Int = 200

    override fun send(vararg event: EventInternal): WebService.Response {
        return WebService.Response(code = code, errorMessage = null)
    }
}

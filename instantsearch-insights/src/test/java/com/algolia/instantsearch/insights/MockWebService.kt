package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.webservice.WebService
import com.algolia.search.model.insights.InsightsEvent
import io.ktor.client.call.HttpClientCall
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import kotlin.coroutines.CoroutineContext

internal class MockWebService : WebService {

    var code: Int = 200

    override suspend fun send(vararg events: InsightsEvent): HttpResponse {
        return object : HttpResponse() {
            override val status: HttpStatusCode
                get() = HttpStatusCode.OK

            override val call: HttpClientCall
                get() = throw NotImplementedError()
            override val content: ByteReadChannel
                get() = throw NotImplementedError()
            override val coroutineContext: CoroutineContext
                get() = throw NotImplementedError()
            override val headers: Headers
                get() = throw NotImplementedError()
            override val requestTime: GMTDate
                get() = throw NotImplementedError()
            override val responseTime: GMTDate
                get() = throw NotImplementedError()
            override val version: HttpProtocolVersion
                get() = throw NotImplementedError()
        }
    }
}

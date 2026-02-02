import com.algolia.client.api.SearchClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.client.model.search.SearchResponse
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

val JsonNoDefaults = Json { encodeDefaults = false }

fun mockClient(
    response: HttpResponseData? = null,
): SearchClient {
    val mockEngine = if (response != null) MockEngine { response } else {
        defaultMockEngine
    }
    return mockClient(mockEngine)
}

fun mockClient(
    mockEngine: MockEngine,
): SearchClient {
    return SearchClient(
        appId = "A",
        apiKey = "B",
        options = ClientOptions(
            engine = mockEngine,
            logLevel = LogLevel.ALL
        )
    )
}

fun respondSearch(response: SearchResponse = responseSearch) = respondJson(response, SearchResponse.serializer())

fun <T> respondJson(response: T, serializer: KSerializer<T>): MockEngine {
    val responseString = JsonNoDefaults.encodeToString(serializer, response)
    return MockEngine {
        respond(
            headers = headersOf(
                "Content-Type",
                listOf(ContentType.Application.Json.toString())
            ),
            content = ByteReadChannel(responseString)
        )
    }
}

fun respondBadRequest(): SearchClient {
    val mockEngine = MockEngine {
        respondBadRequest()
    }
    return SearchClient(
        appId = "A",
        apiKey = "B",
        options = ClientOptions(
            engine = mockEngine,
            logLevel = LogLevel.ALL
        )
    )
}

val defaultMockEngine = MockEngine {
    val responseString = JsonNoDefaults.encodeToString(SearchResponse.serializer(), responseSearch)

    respond(
        headers = headersOf(
            "Content-Type",
            listOf(ContentType.Application.Json.toString())
        ),
        content = ByteReadChannel(responseString)
    )
}

val responseSearch = SearchResponse(hits = emptyList(), query = "", params = "")

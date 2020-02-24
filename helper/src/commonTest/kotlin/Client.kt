import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacets
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val JsonNoDefaults = Json(JsonConfiguration.Stable.copy(encodeDefaults = false))

fun mockClient(
    response: HttpResponseData? = null
): ClientSearch {
    val mockEngine = if (response != null) MockEngine { response } else { defaultMockEngine }
    return mockClient(mockEngine)
}

fun mockClient(
    mockEngine: MockEngine): ClientSearch {
    return ClientSearch(
        ConfigurationSearch(
            ApplicationID("A"),
            APIKey("B"),
            engine = mockEngine,
            logLevel = LogLevel.ALL
        )
    )
}

fun respondSearch(response: ResponseSearch = responseSearch) = respondJson(response, ResponseSearch.serializer())

fun <T> respondJson(response: T, serializer: KSerializer<T>): MockEngine {
    val responseString = JsonNoDefaults.stringify(serializer, response)
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

fun respondBadRequest(): ClientSearch {
    val mockEngine = MockEngine {
        respondBadRequest()
    }
    return ClientSearch(
        ConfigurationSearch(
            ApplicationID("A"),
            APIKey("B"),
            engine = mockEngine,
            logLevel = LogLevel.ALL
        )
    )
}

val defaultMockEngine = MockEngine {
    val responseString = JsonNoDefaults.stringify(ResponseSearch.serializer(), responseSearch)

    respond(
        headers = headersOf(
            "Content-Type",
            listOf(ContentType.Application.Json.toString())
        ),
        content = ByteReadChannel(responseString)
    )
}

val responseSearch = ResponseSearch()
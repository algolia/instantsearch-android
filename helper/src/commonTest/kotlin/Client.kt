import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.logging.LogLevel
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration



fun <T> mockClient(response: T, serializer: KSerializer<T>): ClientSearch {
    val json = Json(JsonConfiguration.Stable.copy(encodeDefaults = false))
    val responseString = json.stringify(serializer, response)
    val mockEngine = MockEngine {
        respond(
            headers = headersOf(
                "Content-Type",
                listOf(ContentType.Application.Json.toString())
            ),
            content = ByteReadChannel(responseString)
        )
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
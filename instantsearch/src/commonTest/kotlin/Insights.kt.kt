import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*

fun mockInsights(
    response: HttpResponseData? = null,
): ClientInsights {
    val mockEngine = if (response != null) MockEngine { response } else {
        defaultMockEngine
    }
    return mockInsights(mockEngine)
}

fun mockInsights(
    mockEngine: MockEngine,
): ClientInsights {
    return ClientInsights(
        ConfigurationInsights(
            ApplicationID("A"),
            APIKey("B"),
            engine = mockEngine,
            logLevel = LogLevel.All
        )
    )
}

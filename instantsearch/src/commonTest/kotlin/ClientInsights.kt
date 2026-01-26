import com.algolia.client.api.InsightsClient
import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.request.HttpResponseData

fun mockClientInsights(
    response: HttpResponseData? = null,
): InsightsClient {
    val mockEngine = if (response != null) MockEngine { response } else {
        defaultMockEngine
    }
    return mockClientInsights(mockEngine)
}

fun mockClientInsights(
    mockEngine: MockEngine,
): InsightsClient {
    return ClientInsights(
        ConfigurationInsights(
            "A",
            "B",
            engine = mockEngine,
            logLevel = LogLevel.All
        )
    )
}

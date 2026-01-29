import com.algolia.client.api.InsightsClient
import com.algolia.client.configuration.ClientOptions
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.logging.LogLevel
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
    return InsightsClient(
        "A",
        "B",
        region = null,
        options = ClientOptions(
            engine = mockEngine,
            logLevel = LogLevel.ALL
        )
    )
}

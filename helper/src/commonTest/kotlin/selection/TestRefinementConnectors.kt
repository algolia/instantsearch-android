package selection

import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

abstract class TestRefinementConnectors {

    protected val color = Attribute("color")
    protected val facets = listOf(
        Facet("blue", 1),
        Facet("red", 2),
        Facet("green", 3)
    )
    private val responseSearch = ResponseSearch(
        hitsOrNull = listOf(),
        facetsOrNull = mapOf(color to facets)
    )
    private val responseString = Json(
        JsonConfiguration.Stable.copy(
            encodeDefaults = false
        )
    ).stringify(
        ResponseSearch.serializer(),
        responseSearch
    )
    private val mockEngine = MockEngine {
        MockHttpResponse(
            call,
            HttpStatusCode.OK,
            headers = headersOf(
                "Content-Type",
                listOf(ContentType.Application.Json.toString())
            ),
            content = ByteReadChannel(responseString)
        )
    }
    private val mockClient = ClientSearch(
        ConfigurationSearch(
            ApplicationID("A"),
            APIKey("B"),
            engine = mockEngine
        )
    )
    protected val mockIndex = mockClient.initIndex(IndexName("index"))
}
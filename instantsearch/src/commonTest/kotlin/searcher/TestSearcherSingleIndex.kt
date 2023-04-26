package searcher

import JsonNoDefaults
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken
import com.algolia.search.model.response.ResponseSearch
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.*
import mockClient
import mockClientInsights
import respondBadRequest
import responseSearch
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull

class TestSearcherSingleIndex {

    private val client = mockClient()
    private val insights = mockClientInsights()
    private val indexName = IndexName("index")
    private val clientError = respondBadRequest()
    private val indexNameError = IndexName("index")

    @Test
    fun searchShouldUpdateLoading() = runTest {
        val searcher = TestSearcherSingle(client, indexName, insights)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        searcher.searchAsync().join()
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() = runTest {
        val searcher = TestSearcherSingle(client, indexName, insights)
        var responded = false

        searcher.response.subscribe { responded = true }
        searcher.response.value.shouldBeNull()
        searcher.searchAsync().join()
        searcher.response.value shouldEqual responseSearch
        responded.shouldBeTrue()
        searcher.error.value.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() = runTest {
        val searcher = TestSearcherSingle(clientError, indexNameError, insights)
        var error = false

        searcher.error.subscribe { error = true }
        searcher.error.value.shouldBeNull()
        searcher.searchAsync().join()
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
        error.shouldBeTrue()
    }

    @Test
    fun searchShouldTriggerViewEvents() = runTest {
        var fetchedObjectIDs = listOf<String>();
        var userToken = "";

        val mockInsightsEngine = MockEngine { request ->
            val json = JsonNoDefaults.decodeFromString(JsonObject.serializer(), (request.body as TextContent).text)
            val event = json["events"]?.jsonArray?.first()?.jsonObject
            fetchedObjectIDs = event?.get("objectIDs")?.jsonArray?.toList()
                    ?.map { it.jsonPrimitive.content }
                    ?: listOf();
            userToken = event?.get("userToken")?.jsonPrimitive?.content ?: ""
            respondOk()
        }
        val mockSearchEngine = MockEngine { request ->
            val responseString = JsonNoDefaults.encodeToString(
                ResponseSearch.serializer(), ResponseSearch(
                    hitsOrNull = (1..3).map { "obj$it" }.map {
                        ResponseSearch.Hit(
                            buildJsonObject { put("objectID", it) })
                    }
                )
            )
            respond(
                headers = headersOf(
                    "Content-Type",
                    listOf(ContentType.Application.Json.toString())
                ),
                content = ByteReadChannel(responseString)
            )
        }
        val searcher =
            TestSearcherSingle(mockClient(mockSearchEngine), indexName, mockClientInsights(mockInsightsEngine), this)
        searcher.searchAsync().join()
        userToken.startsWith("anonymous-").shouldBeTrue()
        fetchedObjectIDs shouldEqual listOf("\"obj1\"", "\"obj2\"", "\"obj3\"")
    }

    @Test
    fun overflowingObjectsShouldBeSplitIntoMultipleEvents() = runTest {
        var eventsCount = 0;

        val mockInsightsEngine = MockEngine { request ->
            val json = JsonNoDefaults.decodeFromString(JsonObject.serializer(), (request.body as TextContent).text)
            eventsCount = json["events"]?.jsonArray?.count() ?: 0
            respondOk()
        }
        val mockSearchEngine = MockEngine { request ->
            val responseString = JsonNoDefaults.encodeToString(
                ResponseSearch.serializer(), ResponseSearch(
                    hitsOrNull = (1..11).map { "obj$it" }.map {
                        ResponseSearch.Hit(
                            buildJsonObject { put("objectID", it) })
                    }
                )
            )
            respond(
                headers = headersOf(
                    "Content-Type",
                    listOf(ContentType.Application.Json.toString())
                ),
                content = ByteReadChannel(responseString)
            )
        }
        val searcher =
            TestSearcherSingle(mockClient(mockSearchEngine), indexName, mockClientInsights(mockInsightsEngine), this)

        searcher.searchAsync().join()
        eventsCount shouldEqual 2
    }

    @Test
    fun testNoEventsSentIfOptOut() = runTest {
        var calledInsights = false;

        val mockInsightsEngine = MockEngine { request ->
            calledInsights = true
            respondOk()
        }
        val mockSearchEngine = MockEngine { request ->
            val responseString = JsonNoDefaults.encodeToString(
                ResponseSearch.serializer(), ResponseSearch(
                    hitsOrNull = (1..11).map { "obj$it" }.map {
                        ResponseSearch.Hit(
                            buildJsonObject { put("objectID", it) })
                    }
                )
            )
            respond(
                headers = headersOf(
                    "Content-Type",
                    listOf(ContentType.Application.Json.toString())
                ),
                content = ByteReadChannel(responseString)
            )
        }
        val searcher =
            TestSearcherSingle(mockClient(mockSearchEngine), indexName, mockClientInsights(mockInsightsEngine), this)
        searcher.isAutoSendingHitsViewEvents = false
        searcher.searchAsync().join()
        calledInsights.shouldBeFalse()
    }

    @Test
    fun shouldPropagateExplicitlyProvidedUserToken() = runTest {
        var userToken = "";

        val mockInsightsEngine = MockEngine { request ->
            val json = JsonNoDefaults.decodeFromString(JsonObject.serializer(), (request.body as TextContent).text)
            userToken =
                json["events"]?.jsonArray?.first()?.jsonObject?.get("userToken")?.jsonPrimitive?.content.toString()
            respondOk()
        }
        val mockSearchEngine = MockEngine { request ->
            val responseString = JsonNoDefaults.encodeToString(
                ResponseSearch.serializer(), ResponseSearch(
                    hitsOrNull = (1..11).map { "obj$it" }.map {
                        ResponseSearch.Hit(
                            buildJsonObject { put("objectID", it) })
                    }
                )
            )
            respond(
                headers = headersOf(
                    "Content-Type",
                    listOf(ContentType.Application.Json.toString())
                ),
                content = ByteReadChannel(responseString)
            )
        }
        val searcher =
            TestSearcherSingle(mockClient(mockSearchEngine), indexName, mockClientInsights(mockInsightsEngine), this)
        searcher.userToken = UserToken("my-user-token");
        searcher.searchAsync().join()

        userToken shouldEqual "my-user-token"
    }

}

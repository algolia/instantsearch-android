package searcher

import JsonNoDefaults
import com.algolia.client.api.CompositionClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.client.model.composition.Params
import com.algolia.client.model.composition.SearchResultsItem
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.composition.CompositionFacetsSearcher
import com.algolia.instantsearch.searcher.composition.CompositionSearcher
import com.algolia.instantsearch.searcher.composition.internal.annotateDisjunctiveFacets
import com.algolia.instantsearch.searcher.composition.internal.toCompositionParams
import com.algolia.instantsearch.searcher.composition.internal.toSearchResponse
import com.algolia.instantsearch.searcher.connectFilterState
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull
import com.algolia.client.model.composition.FacetHits as CompositionFacetHits
import com.algolia.client.model.composition.Hit as CompositionHit
import com.algolia.client.model.composition.SearchForFacetValuesResponse as CompositionSearchForFacetValuesResponse
import com.algolia.client.model.composition.SearchForFacetValuesResults as CompositionSearchForFacetValuesResults
import com.algolia.client.model.composition.SearchResponse as CompositionSearchResponse

class TestCompositionSearcher {

    private val compositionID = "my-composition"

    private val runResponse = CompositionSearchResponse(
        results = listOf(
            SearchResultsItem(
                hits = listOf(CompositionHit(objectID = "obj1"), CompositionHit(objectID = "obj2")),
                hitsPerPage = 20,
                nbHits = 2,
                nbPages = 1,
                page = 0,
                params = "query=phone",
                query = "phone",
                compositions = emptyMap(),
                facets = mapOf("color" to mapOf("red" to 1, "blue" to 2)),
                queryID = "queryID",
                processingTimeMS = 7,
            )
        )
    )

    private val facetValuesResponse = CompositionSearchForFacetValuesResponse(
        results = listOf(
            CompositionSearchForFacetValuesResults(
                indexName = "myIndex",
                facetHits = listOf(CompositionFacetHits(value = "red", highlighted = "<em>red</em>", count = 3)),
                exhaustiveFacetsCount = true,
                processingTimeMS = 2,
            )
        )
    )

    private fun mockCompositionClient(engine: MockEngine): CompositionClient {
        return CompositionClient(
            appId = "A",
            apiKey = "B",
            options = ClientOptions(
                engine = engine,
                logLevel = LogLevel.ALL
            )
        )
    }

    private fun respondRun(): CompositionClient {
        val responseString = JsonNoDefaults.encodeToString(CompositionSearchResponse.serializer(), runResponse)
        return mockCompositionClient(
            MockEngine {
                respond(
                    headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString())),
                    content = ByteReadChannel(responseString)
                )
            }
        )
    }

    private fun respondFacetValues(): CompositionClient {
        val responseString =
            JsonNoDefaults.encodeToString(CompositionSearchForFacetValuesResponse.serializer(), facetValuesResponse)
        return mockCompositionClient(
            MockEngine {
                respond(
                    headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString())),
                    content = ByteReadChannel(responseString)
                )
            }
        )
    }

    @Test
    fun searchShouldMapCompositionRunResponse() = runTest {
        val searcher = CompositionSearcher(
            client = respondRun(),
            compositionID = compositionID,
            coroutineScope = TestCoroutineScope,
        )
        searcher.searchAsync().join()
        searcher.response.value.shouldNotBeNull()
        val response = searcher.response.value!!
        response.hits.map { it.objectID } shouldEqual listOf("obj1", "obj2")
        response.nbHits shouldEqual 2
        response.page shouldEqual 0
        response.nbPages shouldEqual 1
        response.hitsPerPage shouldEqual 20
        response.query shouldEqual "phone"
        response.queryID shouldEqual "queryID"
        response.processingTimeMS shouldEqual 7
        response.facets shouldEqual mapOf("color" to mapOf("red" to 1, "blue" to 2))
        searcher.error.value.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() = runTest {
        val searcher = CompositionSearcher(
            client = mockCompositionClient(MockEngine { respondBadRequest() }),
            compositionID = compositionID,
            coroutineScope = TestCoroutineScope,
        )
        searcher.searchAsync().join()
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
    }

    @Test
    fun setQueryShouldUpdateQuery() {
        val searcher = CompositionSearcher(
            client = respondRun(),
            compositionID = compositionID,
            coroutineScope = TestCoroutineScope,
        )
        searcher.setQuery("phone")
        searcher.query.query shouldEqual "phone"
    }

    @Test
    fun connectFilterStateShouldUpdateFilters() {
        val searcher = CompositionSearcher(
            client = respondRun(),
            compositionID = compositionID,
            coroutineScope = TestCoroutineScope,
        )
        val filter = Filter.Facet("color", "red")
        val filterState = FilterState(mapOf(FilterGroupID("color", FilterOperator.Or) to setOf(filter)))
        searcher.connectFilterState(filterState)
        searcher.query.filters shouldEqual "(\"color\":\"red\")"
        searcher.filterGroups shouldEqual setOf(FilterGroup.Or.Facet(setOf(filter), name = "color"))
    }

    @Test
    fun searchForFacetValuesShouldMapResponse() = runTest {
        val searcher = CompositionFacetsSearcher(
            client = respondFacetValues(),
            compositionID = compositionID,
            attribute = "color",
            coroutineScope = TestCoroutineScope,
        )
        searcher.searchAsync().join()
        searcher.response.value.shouldNotBeNull()
        val response = searcher.response.value!!
        response.facetHits.size shouldEqual 1
        response.facetHits.first().value shouldEqual "red"
        response.facetHits.first().highlighted shouldEqual "<em>red</em>"
        response.facetHits.first().count shouldEqual 3
        response.exhaustiveFacetsCount.shouldBeTrue()
        response.processingTimeMS shouldEqual 2
    }

    @Test
    fun searchParamsShouldMapToCompositionParams() {
        val params = SearchParamsObject(
            query = "phone",
            filters = "(\"color\":\"red\")",
            facets = listOf("color", "brand"),
            page = 2,
            hitsPerPage = 50,
            highlightPreTag = "<em>",
            highlightPostTag = "</em>",
        ).toCompositionParams()

        params.query shouldEqual "phone"
        params.filters shouldEqual "(\"color\":\"red\")"
        params.facets shouldEqual listOf("color", "brand")
        params.page shouldEqual 2
        params.hitsPerPage shouldEqual 50
    }

    @Test
    fun annotateDisjunctiveFacetsShouldTagRefinedAttributes() {
        val params = Params(facets = listOf("color", "brand"))
        val annotated = params.annotateDisjunctiveFacets(setOf("color"))
        annotated.facets shouldEqual listOf("disjunctive(color)", "brand")
    }

    @Test
    fun searchResultsItemShouldMapToSearchResponse() {
        val response = runResponse.results.first().toSearchResponse()
        response.hits.map { it.objectID } shouldEqual listOf("obj1", "obj2")
        response.query shouldEqual "phone"
        response.params shouldEqual "query=phone"
        response.nbHits shouldEqual 2
        response.facets shouldEqual mapOf("color" to mapOf("red" to 1, "blue" to 2))
        response.queryID shouldEqual "queryID"
    }
}

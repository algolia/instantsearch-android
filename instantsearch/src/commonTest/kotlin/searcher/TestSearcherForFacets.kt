package searcher

import com.algolia.client.model.search.SearchForFacetValuesResponse
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import respondBadRequest
import respondJson
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull

class TestSearcherForFacets {

    private val attribute = "color"
    private val response = SearchForFacetValuesResponse(
        facetHits = emptyList(),
        exhaustiveFacetsCount = true,
        processingTimeMS = 0
    )
    private val client = mockClient(respondJson(response, SearchForFacetValuesResponse.serializer()))
    private val indexName = "index"
    private val clientError = respondBadRequest()
    private val indexNameError = "index"

    @Test
    fun searchShouldUpdateLoading() = runTest {
        val searcher = TestSearcherForFacets(client, indexName, attribute)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        searcher.searchAsync().join()
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() = runTest {
        val searcher = TestSearcherForFacets(client, indexName, attribute)
        var responded = false

        searcher.response.subscribe { responded = true }
        searcher.response.value.shouldBeNull()
        searcher.searchAsync().join()
        searcher.response.value shouldEqual response
        responded.shouldBeTrue()
        searcher.error.value.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() = runTest {
        val searcher = TestSearcherForFacets(clientError, indexNameError, attribute)
        var error = false

        searcher.error.subscribe { error = true }
        searcher.error.value.shouldBeNull()
        searcher.searchAsync().join()
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
        error.shouldBeTrue()
    }
}

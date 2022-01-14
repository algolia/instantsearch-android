package searcher

import blocking
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearchForFacets
import kotlin.test.Test
import mockClient
import respondBadRequest
import respondJson
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull

class TestSearcherForFacets {

    private val attribute = Attribute("color")
    private val response = ResponseSearchForFacets(
        facets = listOf(),
        exhaustiveFacetsCount = true,
        processingTimeMS = 0
    )
    private val client = mockClient(respondJson(response, ResponseSearchForFacets.serializer()))
    private val indexName = IndexName("index")
    private val clientError = respondBadRequest()
    private val indexNameError = IndexName("index")

    @Test
    fun searchShouldUpdateLoading() {
        val searcher = TestSearcherForFacets(client, indexName, attribute)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        blocking { searcher.searchAsync().join() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = TestSearcherForFacets(client, indexName, attribute)
        var responded = false

        searcher.response.subscribe { responded = true }
        searcher.response.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.response.value shouldEqual response
        responded.shouldBeTrue()
        searcher.error.value.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() {
        val searcher = TestSearcherForFacets(clientError, indexNameError, attribute)
        var error = false

        searcher.error.subscribe { error = true }
        searcher.error.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
        error.shouldBeTrue()
    }
}

package searcher

import blocking
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearchForFacets
import io.ktor.client.engine.mock.respondBadRequest
import mockClient
import respondJson
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSearcherForFacets  {

    private val attribute = Attribute("color")
    private val response = ResponseSearchForFacets(
        facets = listOf(),
        exhaustiveFacetsCount = true,
        processingTimeMS = 0
    )
    private val client = mockClient(respondJson(response, ResponseSearchForFacets.serializer()))
    private val index = client.initIndex(IndexName("index"))
    private val clientError = mockClient(respondBadRequest())
    private val indexError = clientError.initIndex(IndexName("index"))

    @Test
    fun searchShouldUpdateLoading() {
        val searcher = SearcherForFacets(index, attribute)
        var count = 0

        searcher.onLoadingChanged += { if (it) count++ }
        searcher.loading.shouldBeFalse()
        blocking { searcher.search() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = SearcherForFacets(index, attribute)
        var responded = false

        searcher.onResponseChanged += { responded = true }
        searcher.response.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.response shouldEqual response
        responded.shouldBeTrue()
        searcher.error.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() {
        val searcher = SearcherForFacets(indexError, attribute)
        var error = false

        searcher.onErrorChanged += { error = true }
        searcher.error.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.shouldNotBeNull()
        searcher.response.shouldBeNull()
        error.shouldBeTrue()
    }
}
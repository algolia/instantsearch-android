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

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.get().shouldBeFalse()
        blocking { searcher.search() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = SearcherForFacets(index, attribute)
        var responded = false

        searcher.response.subscribe { responded = true }
        searcher.response.get().shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.response.get() shouldEqual response
        responded.shouldBeTrue()
        searcher.error.get().shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() {
        val searcher = SearcherForFacets(indexError, attribute)
        var error = false

        searcher.error.subscribe {  error = true }
        searcher.error.get().shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.get().shouldNotBeNull()
        searcher.response.get().shouldBeNull()
        error.shouldBeTrue()
    }
}
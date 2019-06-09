package searcher

import blocking
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import io.ktor.client.engine.mock.respondBadRequest
import mockClient
import responseSearch
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSearcherSingleIndex {

    private val client = mockClient()
    private val index = client.initIndex(IndexName("index"))
    private val clientError = mockClient(respondBadRequest())
    private val indexError = clientError.initIndex(IndexName("index"))

    @Test
    fun searchShouldUpdateLoading() {
        val searcher = SearcherSingleIndex(index)
        var count = 0

        searcher.onLoadingChanged += { if (it) count++ }
        searcher.loading.shouldBeFalse()
        blocking { searcher.search() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = SearcherSingleIndex(index)
        var responded = false

        searcher.onResponseChanged += { responded = true }
        searcher.response.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.response shouldEqual responseSearch
        responded.shouldBeTrue()
        searcher.error.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() {
        val searcher = SearcherSingleIndex(indexError)
        var error = false

        searcher.onErrorChanged += { error = true }
        searcher.error.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.shouldNotBeNull()
        searcher.response.shouldBeNull()
        error.shouldBeTrue()
    }
}
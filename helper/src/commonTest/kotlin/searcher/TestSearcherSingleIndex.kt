package searcher

import blocking
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import io.ktor.client.engine.mock.respondBadRequest
import kotlinx.coroutines.Dispatchers
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
        val searcher = SearcherSingleIndex(index, dispatcher = Dispatchers.Default)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        blocking { searcher.search() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = SearcherSingleIndex(index, dispatcher = Dispatchers.Default)
        var responded = false

        searcher.response.subscribe { responded = true }
        searcher.response.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.response.value shouldEqual responseSearch
        responded.shouldBeTrue()
        searcher.error.value.shouldBeNull()
    }

    @Test
    fun searchShouldUpdateError() {
        val searcher = SearcherSingleIndex(indexError, dispatcher = Dispatchers.Default)
        var error = false

        searcher.error.subscribe { error = true }
        searcher.error.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
        error.shouldBeTrue()
    }
}
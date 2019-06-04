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

        searcher.onLoadingChanged += { it shouldEqual searcher.loading }
        searcher.loading.shouldBeFalse()
        blocking {
            val job = searcher.search()

            searcher.loading.shouldBeTrue()
            job.join()
        }
        searcher.loading.shouldBeFalse()
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = SearcherSingleIndex(index)
        var responded = false

        searcher.onResponseChanged += { responded = true }
        searcher.response.shouldBeNull()
        blocking { searcher.search().join() }
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
        blocking { searcher.search().join() }
        searcher.error.shouldNotBeNull()
        searcher.response.shouldBeNull()
        error.shouldBeTrue()
    }
}
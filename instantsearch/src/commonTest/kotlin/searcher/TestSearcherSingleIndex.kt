package searcher

import blocking
import com.algolia.search.model.IndexName
import kotlin.test.Test
import mockClient
import respondBadRequest
import responseSearch
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull

class TestSearcherSingleIndex {

    private val client = mockClient()
    private val indexName = IndexName("index")
    private val clientError = respondBadRequest()
    private val indexNameError = IndexName("index")

    @Test
    fun searchShouldUpdateLoading() {
        val searcher = TestSearcherSingle(client, indexName)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        blocking { searcher.searchAsync().join() }
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() {
        val searcher = TestSearcherSingle(client, indexName)
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
        val searcher = TestSearcherSingle(clientError, indexNameError)
        var error = false

        searcher.error.subscribe { error = true }
        searcher.error.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        searcher.error.value.shouldNotBeNull()
        searcher.response.value.shouldBeNull()
        error.shouldBeTrue()
    }
}

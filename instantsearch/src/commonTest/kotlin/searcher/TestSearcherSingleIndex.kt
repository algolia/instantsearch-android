package searcher

import com.algolia.search.model.IndexName
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import mockInsights
import respondBadRequest
import responseSearch
import shouldBeFalse
import shouldBeNull
import shouldBeTrue
import shouldEqual
import shouldNotBeNull

class TestSearcherSingleIndex {

    private val client = mockClient()
    private val insights = mockInsights()
    private val indexName = IndexName("index")
    private val clientError = respondBadRequest()
    private val indexNameError = IndexName("index")

    @Test
    fun searchShouldUpdateLoading() = runTest {
        val searcher = TestSearcherSingle(client, insights, indexName)
        var count = 0

        searcher.isLoading.subscribe { if (it) count++ }
        searcher.isLoading.value.shouldBeFalse()
        searcher.searchAsync().join()
        count shouldEqual 1
    }

    @Test
    fun searchShouldUpdateResponse() = runTest {
        val searcher = TestSearcherSingle(client, insights, indexName)
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
        val searcher = TestSearcherSingle(clientError, insights, indexNameError)
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
        var searcher = TestSearcherSingle(client, insights, indexName)
    }
}

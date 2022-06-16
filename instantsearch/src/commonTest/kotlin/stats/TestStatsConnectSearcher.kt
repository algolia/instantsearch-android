package stats

import com.algolia.instantsearch.stats.StatsViewModel
import com.algolia.instantsearch.stats.connectSearcher
import com.algolia.search.model.IndexName
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import mockClient
import responseSearch
import searcher.TestSearcherSingle
import shouldBeNull
import shouldEqual

class TestStatsConnectSearcher {

    private val client = mockClient()
    private val indexName = IndexName("A")

    @Test
    fun connectShouldSetItem() {
        val searcher = TestSearcherSingle(client, indexName).also {
            it.response.value = responseSearch
        }
        val viewModel = StatsViewModel()
        val connection = viewModel.connectSearcher(searcher)

        viewModel.response.value.shouldBeNull()
        connection.connect()
        viewModel.response.value shouldEqual responseSearch
    }

    @Test
    fun onResponseChangedShouldSetItem() = runTest {
        val searcher = TestSearcherSingle(client, indexName)
        val viewModel = StatsViewModel()
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        viewModel.response.value.shouldBeNull()
        searcher.searchAsync().join()
        viewModel.response.value shouldEqual responseSearch
    }
}

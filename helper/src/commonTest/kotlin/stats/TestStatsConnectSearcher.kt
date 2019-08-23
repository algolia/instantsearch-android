package stats

import blocking
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectSearcher
import com.algolia.search.model.IndexName
import mockClient
import responseSearch
import searcher.TestSearcherSingle
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestStatsConnectSearcher {

    private val client = mockClient()
    private val index = client.initIndex(IndexName("A"))

    @Test
    fun connectShouldSetItem() {
        val searcher = TestSearcherSingle(index).also {
            it.response.value = responseSearch
        }
        val viewModel = StatsViewModel()
        val connection = viewModel.connectSearcher(searcher)

        viewModel.response.value.shouldBeNull()
        connection.connect()
        viewModel.response.value shouldEqual responseSearch
    }

    @Test
    fun onResponseChangedShouldSetItem() {
        val searcher = TestSearcherSingle(index)
        val viewModel = StatsViewModel()
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        viewModel.response.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        viewModel.response.value shouldEqual responseSearch
    }
}
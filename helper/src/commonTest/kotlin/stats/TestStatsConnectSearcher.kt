package stats

import blocking
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectSearcher
import com.algolia.search.model.IndexName
import mockClient
import responseSearch
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestStatsConnectSearcher {

    private val client = mockClient()
    private val index = client.initIndex(IndexName("A"))

    @Test
    fun connectShouldSetItem() {
        val searcher = SearcherSingleIndex(index).also { it.response.set(responseSearch) }
        val viewModel = StatsViewModel()

        viewModel.item.shouldBeNull()
        viewModel.connectSearcher(searcher)
        viewModel.item shouldEqual responseSearch
    }

    @Test
    fun onResponseChangedShouldSetItem() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = StatsViewModel()

        viewModel.connectSearcher(searcher)
        viewModel.item.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        viewModel.item shouldEqual responseSearch
    }
}
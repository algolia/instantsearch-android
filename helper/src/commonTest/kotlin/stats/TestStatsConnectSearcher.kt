package stats

import blocking
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectSearcher
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import mockClient
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestStatsConnectSearcher {

    private val client = mockClient(ResponseSearch(), ResponseSearch.serializer())
    private val index = client.initIndex(IndexName("A"))
    private val response = ResponseSearch()

    @Test
    fun connectShouldSetItem() {
        val searcher = SearcherSingleIndex(index).also { it.response = response }
        val viewModel = StatsViewModel()

        viewModel.item.shouldBeNull()
        viewModel.connectSearcher(searcher)
        viewModel.item shouldEqual response
    }

    @Test
    fun onResponseChangedShouldSetItem() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = StatsViewModel()

        viewModel.connectSearcher(searcher)
        viewModel.item.shouldBeNull()
        blocking { searcher.search().join() }
        viewModel.item shouldEqual response
    }
}
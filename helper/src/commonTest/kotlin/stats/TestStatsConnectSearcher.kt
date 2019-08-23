package stats

import blocking
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectSearcher
import com.algolia.search.model.IndexName
import kotlinx.coroutines.Dispatchers
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
        val searcher = SearcherSingleIndex(index).also { it.response.value = responseSearch }
        val viewModel = StatsViewModel()
        val connection = viewModel.connectSearcher(searcher)

        viewModel.response.value.shouldBeNull()
        connection.connect()
        viewModel.response.value shouldEqual responseSearch
    }

    @Test
    fun onResponseChangedShouldSetItem() {
        val searcher = SearcherSingleIndex(index, isDisjunctiveFacetingEnabled = false)
        val viewModel = StatsViewModel()
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        viewModel.response.value.shouldBeNull()
        blocking { searcher.searchAsync().join() }
        viewModel.response.value shouldEqual responseSearch
    }
}
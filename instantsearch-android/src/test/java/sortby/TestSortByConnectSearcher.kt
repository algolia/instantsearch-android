package sortby

import com.algolia.instantsearch.helper.sortby.SortByViewModel
import com.algolia.instantsearch.helper.sortby.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import mockClient
import shouldEqual
import kotlin.test.Test


class TestSortByConnectSearcher {

    private val client = mockClient()
    private val indexA = client.initIndex(IndexName("A"))
    private val indexB = client.initIndex(IndexName("B"))

    @Test
    fun connectShouldUpdateSearcherIndex() {
        val searcher = SearcherSingleIndex(indexA)
        val viewModel = SortByViewModel(
            map = mapOf(
                0 to indexA,
                1 to indexB
            )
        ).apply { selected.value = 1 }
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        searcher.index shouldEqual indexB
    }

    @Test
    fun onSelectedComputedShouldUpdateIndex() {
        val searcher = SearcherSingleIndex(indexA)
        val viewModel = SortByViewModel(
            map = mapOf(
                0 to indexA,
                1 to indexB
            )
        )
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        viewModel.eventSelection.send(1)
        searcher.index shouldEqual indexB
    }
}
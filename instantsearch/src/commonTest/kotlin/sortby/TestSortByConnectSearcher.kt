package sortby

import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.sortby.SortByViewModel
import com.algolia.instantsearch.sortby.connectSearcher
import com.algolia.search.model.IndexName
import mockClient
import shouldEqual
import kotlin.test.Test

class TestSortByConnectSearcher {

    private val client = mockClient()
    private val indexNameA = IndexName("A")
    private val indexNameB = IndexName("B")

    @Test
    fun connectShouldUpdateSearcherIndex() {
        val searcher = HitsSearcher(client, indexNameA)
        val viewModel = SortByViewModel(
            map = mapOf(
                0 to indexNameA,
                1 to indexNameB
            )
        ).apply { selected.value = 1 }
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        searcher.indexName shouldEqual indexNameB
    }

    @Test
    fun onSelectedComputedShouldUpdateIndex() {
        val searcher = HitsSearcher(client, indexNameA)
        val viewModel = SortByViewModel(
            map = mapOf(
                0 to indexNameA,
                1 to indexNameB
            )
        )
        val connection = viewModel.connectSearcher(searcher)

        connection.connect()
        viewModel.eventSelection.send(1)
        searcher.indexName shouldEqual indexNameB
    }
}

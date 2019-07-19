package index

import com.algolia.instantsearch.helper.index.IndexSegmentViewModel
import com.algolia.instantsearch.helper.index.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import mockClient
import shouldEqual
import kotlin.test.Test


class TestIndexSegmentConnectSearcher {

    private val client = mockClient()
    private val indexA = client.initIndex(IndexName("A"))
    private val indexB = client.initIndex(IndexName("B"))

    @Test
    fun connectShouldUpdateSearcherIndex() {
        val searcher = SearcherSingleIndex(indexA)
        val viewModel = IndexSegmentViewModel(
            segment = mapOf(
                0 to indexA,
                1 to indexB
            )
        ).apply { selected.value = 1 }

        viewModel.connectSearcher(searcher)
        searcher.index shouldEqual indexB
    }

    @Test
    fun onSelectedComputedShouldUpdateIndex() {
        val searcher = SearcherSingleIndex(indexA)
        val viewModel = IndexSegmentViewModel(
            segment = mapOf(
                0 to indexA,
                1 to indexB
            )
        )

        viewModel.connectSearcher(searcher)
        viewModel.eventSelection.send(1)
        searcher.index shouldEqual indexB
    }
}
package loading

import blocking
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.helper.loading.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import mockClient
import searcher.MockSearcher
import shouldBeFalse
import shouldEqual
import kotlin.test.Test


class TestLoadingConnectSearcher {

    private val client = mockClient()
    private val index = client.initIndex(IndexName("A"))

    @Test
    fun connectShouldSetItem() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = LoadingViewModel()
        val expected = true

        searcher.loading = expected
        viewModel.item.shouldBeFalse()
        viewModel.connectSearcher(searcher)
        viewModel.item shouldEqual expected
    }

    @Test
    fun onLoadingChangedShouldSetItem() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = LoadingViewModel()
        val expected = true

        viewModel.item.shouldBeFalse()
        viewModel.connectSearcher(searcher)
        searcher.loading = true
        viewModel.item shouldEqual expected
    }

    @Test
    fun onTriggeredShouldCallSearch() {
        val searcher = MockSearcher()
        val viewModel = LoadingViewModel()

        searcher.searchCount shouldEqual 0
        viewModel.connectSearcher(searcher)
        viewModel.trigger(Unit)
        blocking { searcher.job?.join() }
        searcher.searchCount shouldEqual 1
    }
}
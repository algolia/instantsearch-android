package loading

import blocking
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
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

        searcher.isLoading.set(expected)
        viewModel.isLoading.get().shouldBeFalse()
        viewModel.connectSearcher(searcher)
        viewModel.isLoading.get() shouldEqual expected
    }

    @Test
    fun onLoadingChangedShouldSetItem() {
        val debouncer = Debouncer(100)
        val searcher = SearcherSingleIndex(index)
        val viewModel = LoadingViewModel()
        val expected = true

        viewModel.isLoading.get().shouldBeFalse()
        viewModel.connectSearcher(searcher, debouncer)
        searcher.isLoading.set(true)
        blocking { debouncer.job!!.join() }
        viewModel.isLoading.get() shouldEqual expected
    }

    @Test
    fun onEventSentShouldCallSearch() {
        val searcher = MockSearcher()
        val viewModel = LoadingViewModel()

        searcher.searchCount shouldEqual 0
        viewModel.connectSearcher(searcher)
        viewModel.event.send(Unit)
        blocking { searcher.job?.join() }
        searcher.searchCount shouldEqual 1
    }
}
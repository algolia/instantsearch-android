package loading

import blocking
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.loading.connectionSearcher
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
        val debouncer = Debouncer(200)
        val connection = viewModel.connectionSearcher(searcher, debouncer)

        searcher.isLoading.value = expected
        viewModel.isLoading.value.shouldBeFalse()
        connection.connect()
        viewModel.isLoading.value shouldEqual expected
    }

    @Test
    fun onLoadingChangedShouldSetItem() {
        val debouncer = Debouncer(100)
        val searcher = SearcherSingleIndex(index)
        val viewModel = LoadingViewModel()
        val expected = true
        val connection = viewModel.connectionSearcher(searcher, debouncer)

        viewModel.isLoading.value.shouldBeFalse()
        connection.connect()
        searcher.isLoading.value = true
        blocking { debouncer.job!!.join() }
        viewModel.isLoading.value shouldEqual expected
    }

    @Test
    fun onEventSentShouldCallSearch() {
        val searcher = MockSearcher()
        val viewModel = LoadingViewModel()
        val debouncer = Debouncer(200)
        val connection = viewModel.connectionSearcher(searcher, debouncer)

        searcher.searchCount shouldEqual 0
        connection.connect()
        viewModel.eventReload.send(Unit)
        blocking { searcher.job?.join() }
        searcher.searchCount shouldEqual 1
    }
}
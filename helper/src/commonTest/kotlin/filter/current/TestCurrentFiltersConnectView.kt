package filter.current

import com.algolia.instantsearch.helper.filter.current.CurrentFiltersView
import com.algolia.instantsearch.helper.filter.current.CurrentFiltersViewModel
import com.algolia.instantsearch.helper.filter.current.connectView
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeEmpty
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestCurrentFiltersConnectView {

    private val identifier: String = "id"
    private val filterRed = Filter.Facet(Attribute("color"), "red")
    private val filters: Map<String, Filter> = mapOf(identifier to filterRed)

    private class MockCurrentFiltersView : CurrentFiltersView {
        var filters: Map<String, Filter> = mapOf()

        override fun setItem(item: Map<String, Filter>) {
            filters = item
        }

        override var onClick: ((String) -> Unit)? = null
    }

    @Test
    fun connectShouldSetItems() {
        val viewModel = CurrentFiltersViewModel(filters)
        val view = MockCurrentFiltersView()

        viewModel.connectView(view)
        view.filters shouldEqual filters
    }

    @Test
    fun onClickShouldCallClear() {
        val viewModel = CurrentFiltersViewModel(filters)
        val view = MockCurrentFiltersView()

        viewModel.onMapComputed += {
            viewModel.item = it
        }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(identifier)
        view.filters.shouldBeEmpty()
    }

    @Test
    fun onItemChangedShouldCallSetItems() {
        val viewModel = CurrentFiltersViewModel(filters)
        val view = MockCurrentFiltersView()

        viewModel.connectView(view)
        viewModel.map = mapOf()
        view.filters shouldEqual mapOf()
    }
}
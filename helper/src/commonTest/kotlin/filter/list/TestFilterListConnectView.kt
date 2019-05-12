package filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.list.FilterListView
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestFilterListConnectView  {

    private val color = Attribute("color")
    private val filter = Filter.Facet(color, "red")
    private val filters = listOf(filter)
    private val selections = setOf(filter)

    private class MockFilterListViewFacet : FilterListView<Filter.Facet> {

        var items: List<SelectableItem<Filter.Facet>> = listOf()

        override var onClick: ((Filter.Facet) -> Unit)? = null

        override fun setSelectableItems(selectableItems: List<SelectableItem<Filter.Facet>>) {
            items = selectableItems
        }
    }

    @Test
    fun connectShouldCallSetSelectableItems() {
        val view = MockFilterListViewFacet()
        val viewModel = FilterListViewModel.Facet(filters, selections)

        viewModel.connectView(view)
        view.items shouldEqual listOf(filter to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockFilterListViewFacet()
        val viewModel = FilterListViewModel.Facet(filters)

        viewModel.onSelectionsComputed += { viewModel.selections = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(filter)
        view.items shouldEqual listOf(filter to true)
    }

    @Test
    fun onItemsChangedShouldCallSetSelectableItems() {
        val view = MockFilterListViewFacet()
        val viewModel = FilterListViewModel.Facet()

        viewModel.connectView(view)
        viewModel.items = filters
        view.items shouldEqual listOf(filter to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetSelectableItems() {
        val view = MockFilterListViewFacet()
        val viewModel = FilterListViewModel.Facet(filters)

        viewModel.connectView(view)
        viewModel.selections = selections
        view.items shouldEqual listOf(filter to true)
    }
}
package filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.FacetListItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.search.model.search.Facet
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestFacetListConnectView  {

    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val selections = setOf(red.value)

    private class MockSelectableFacetsView : FacetListView {

        var items: List<FacetListItem> = listOf()

        override var onClick: ((Facet) -> Unit)? = null

        override fun setItem(item: List<SelectableItem<Facet>>) {
            items = item
        }
    }

    @Test
    fun connectShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel(facets)

        viewModel.selections = selections
        viewModel.connectView(view)
        view.items shouldEqual listOf(red to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel(facets)

        viewModel.onSelectionsComputed += { viewModel.selections = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(red)
        view.items shouldEqual listOf(red to true)
    }

    @Test
    fun onItemsChangedShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel()

        viewModel.connectView(view)
        viewModel.item = facets
        view.items shouldEqual listOf(red to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel(facets)

        viewModel.connectView(view)
        viewModel.selections = selections
        view.items shouldEqual listOf(red to true)
    }
}
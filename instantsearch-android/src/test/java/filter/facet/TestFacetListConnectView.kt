package filter.facet

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.FacetListItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.search.model.search.Facet
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test

class TestFacetListConnectView {

    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val selections = setOf(red.value)

    private class MockSelectableFacetsView : FacetListView {

        override var onSelection: Callback<Facet>? = null

        var list: List<FacetListItem> = listOf()

        override fun setItems(items: List<SelectableItem<Facet>>) {
            list = items
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel(facets)
        val connection = viewModel.connectView(view)

        viewModel.selections.value = selections
        connection.connect()
        view.list shouldEqual listOf(red to true)
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel()
        val connection = viewModel.connectView(view)

        connection.connect()
        viewModel.items.value = facets
        view.list shouldEqual listOf(red to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetItem() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel(facets)
        val connection = viewModel.connectView(view)

        connection.connect()
        viewModel.selections.value = selections
        view.list shouldEqual listOf(red to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableFacetsView()
        val viewModel = FacetListViewModel(facets)
        val connection = viewModel.connectView(view)

        viewModel.eventSelection.subscribe { viewModel.selections.value = it }
        connection.connect()
        view.onSelection.shouldNotBeNull()
        view.onSelection!!(red)
        view.list shouldEqual listOf(red to true)
    }
}

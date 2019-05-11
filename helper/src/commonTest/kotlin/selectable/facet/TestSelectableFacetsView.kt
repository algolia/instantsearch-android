package selectable.facet

import com.algolia.search.model.search.Facet
import selectable.list.SelectableItem
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestSelectableFacetsView  {

    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val selections = setOf(red.value)

    private class MockSelectableFacetsView : SelectableFacetsView {

        var items: List<SelectableItem<Facet>> = listOf()

        override var onClick: ((Facet) -> Unit)? = null

        override fun setSelectableItems(selectableItems: List<SelectableItem<Facet>>) {
            items = selectableItems
        }
    }

    @Test
    fun connectShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableFacetsViewModel(facets, selections)

        viewModel.connectView(view)
        view.items shouldEqual listOf(red to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableFacetsViewModel(facets)

        viewModel.onSelectionsComputed += { viewModel.selections = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(red)
        view.items shouldEqual listOf(red to true)
    }

    @Test
    fun onItemsChangedShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableFacetsViewModel()

        viewModel.connectView(view)
        viewModel.items = facets
        view.items shouldEqual listOf(red to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetSelectableItems() {
        val view = MockSelectableFacetsView()
        val viewModel = SelectableFacetsViewModel(facets)

        viewModel.connectView(view)
        viewModel.selections = selections
        view.items shouldEqual listOf(red to true)
    }
}
package refinement.facet

import com.algolia.search.model.search.Facet
import selection.list.SelectableItem
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestRefinementFacetsView  {

    private val red = Facet("red", 1)
    private val facets = listOf(red)
    private val selections = setOf(red.value)

    private class MockRefinementFacetsView : RefinementFacetsView {

        var items: List<SelectableItem<Facet>> = listOf()

        override var onClick: ((Facet) -> Unit)? = null

        override fun setSelectableItems(selectableItems: List<SelectableItem<Facet>>) {
            items = selectableItems
        }
    }

    @Test
    fun connectShouldCallSetSelectableItems() {
        val view = MockRefinementFacetsView()
        val viewModel = RefinementFacetsViewModel(facets, selections)

        viewModel.connectView(view)
        view.items shouldEqual listOf(red to true)
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockRefinementFacetsView()
        val viewModel = RefinementFacetsViewModel(facets)

        viewModel.onSelectionsComputed += { viewModel.selections = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(red)
        view.items shouldEqual listOf(red to true)
    }

    @Test
    fun onItemsChangedShouldCallSetSelectableItems() {
        val view = MockRefinementFacetsView()
        val viewModel = RefinementFacetsViewModel()

        viewModel.connectView(view)
        viewModel.items = facets
        view.items shouldEqual listOf(red to false)
    }

    @Test
    fun onSelectionsChangedShouldCallSetSelectableItems() {
        val view = MockRefinementFacetsView()
        val viewModel = RefinementFacetsViewModel(facets)

        viewModel.connectView(view)
        viewModel.selections = selections
        view.items shouldEqual listOf(red to true)
    }
}
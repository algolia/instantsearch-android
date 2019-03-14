package numericControl

import Facet
import MockSearcher
import facets
import otherFacets
import shouldEqual
import kotlin.test.Test

class TestNumericControl {
    private class MockView : NumericControlView<Facet> {
        lateinit var click: (Facet) -> Unit
        var data = mutableListOf<Facet>()
        var dataSelected: Facet? = null

        override fun setRefinements(refinements: List<Facet>) {
            data = refinements.toMutableList()
        }

        override fun setSelected(refinement: Facet?) {
            dataSelected = refinement
        }

        override fun setDefaultChoice(choice: String) {
            TODO("not implemented")
        }

        override fun onClickRefinement(onClick: (Facet) -> Unit) {
            click = { onClick(it) }
        }
    }

    private fun getViews() = listOf(MockView(), MockView(), MockView())

    private fun setup(
        model: NumericControlModel<Facet>,
        views: List<MockView>,
        searcher: MockSearcher = MockSearcher()
    ) {
        model.connectViews(views)
        model.connectSearcher(searcher)
        model.refinements = facets
        views.first().click(facets[2])
        model.refinements shouldEqual facets
        views.forEach { it.data shouldEqual facets }
        searcher.count shouldEqual 1
    }

    @Test
    fun refinements() {
        val model = NumericControlModel<Facet>()
        val views = getViews()
        setup(model, views)
        model.selected shouldEqual facets[2]
        views.forEach { it.dataSelected shouldEqual it.dataSelected}
        model.refinements = otherFacets
        model.selected shouldEqual null
        views.forEach { it.dataSelected shouldEqual null }
    }
}
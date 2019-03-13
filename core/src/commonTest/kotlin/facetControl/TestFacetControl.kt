package facetControl

import Facet
import MockSearcher
import facets
import shouldBeTrue
import shouldEqual
import kotlin.test.Test

class TestFacetControl {
    private class MockView : FacetControlView<Facet> {
        lateinit var click: (Facet) -> Unit
        var data: Facet? = null
        var dataSelected = false

        override fun setRefinement(value: Facet?) {
            data = value
        }

        override fun onClickRefinement(onClick: (Facet) -> Unit) {
            click = { onClick(it) }
        }

        override fun setSelected(isActive: Boolean) {
            dataSelected = isActive
        }

    }

    private fun getViews() = listOf(MockView(), MockView(), MockView())

    private fun setup(
        model: FacetControlModel<Facet>,
        views: List<MockView>,
        searcher: MockSearcher = MockSearcher()
    ) {
        model.connectViews(views)
        model.connectSearcher(searcher)
        model.refinement = facets[0]
        views.first().click(facets[0])
        model.refinement shouldEqual facets[0]
        views.forEach { it.data shouldEqual facets[0] }
        searcher.count shouldEqual 1
    }

    @Test
    fun refinement() {
        val model = FacetControlModel<Facet>()
        val views = getViews()
        setup(model, views)
        model.selected shouldEqual facets[0]
        views.forEach { it.dataSelected.shouldBeTrue() }
        model.refinement = facets[1]
        model.selected shouldEqual null
        views.forEach { it.dataSelected shouldEqual false }
    }
}
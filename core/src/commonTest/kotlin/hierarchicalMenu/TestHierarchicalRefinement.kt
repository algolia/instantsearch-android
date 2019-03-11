package hierarchicalMenu

import Facet
import MockSearcher
import facets
import helper.Hierarchy
import otherFacets
import shouldEqual
import kotlin.test.Test


class TestHierarchicalRefinement {
    private class MockView : HierarchicalView<Facet> {
        lateinit var click: (Facet) -> Unit
        var data: Hierarchy<Facet> = mapOf()
        var dataSelected: Facet? = null

        override fun setHierarchy(hierarchy: Hierarchy<Facet>) {
            data = hierarchy
        }

        override fun setSelected(refinement: Facet?) {
            dataSelected = refinement
        }

        override fun onClickRefinement(onClick: (Facet) -> Unit) {
            click = { refinement: Facet ->
                onClick(refinement)
            }
        }
    }

    private fun getViews() = listOf(MockView(), MockView(), MockView())

    private val facetsHierarchy = mapOf("color" to facets)
    private val otherFacetsHierarchy = mapOf("color" to otherFacets)

    private fun setup(
        model: HierarchicalRefinementModel<Facet>,
        views: List<MockView>,
        searcher: MockSearcher = MockSearcher()
    ) {
        model.connectViews(views)
        model.connectSearcher(searcher)
        model.hierarchicalRefinements = facetsHierarchy
        facets.forEach {
            views.first().click(it)
        }
        model.hierarchicalRefinements shouldEqual facetsHierarchy
        views.forEach {
            it.data shouldEqual facetsHierarchy
        }
        searcher.count shouldEqual facets.size
    }

    @Test
    fun refinement() {
        val model = HierarchicalRefinementModel<Facet>()
        val views = getViews()
        setup(model, views)
        model.selected shouldEqual facets.last()
        views.forEach {
            it.dataSelected shouldEqual facets.last()
        }
        model.hierarchicalRefinements = otherFacetsHierarchy
        model.selected shouldEqual null
        views.forEach {
            it.data shouldEqual otherFacetsHierarchy
            it.dataSelected shouldEqual null
        }
    }
}
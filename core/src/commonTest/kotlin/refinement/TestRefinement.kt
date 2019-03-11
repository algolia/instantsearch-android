package refinement

import Facet
import MockSearcher
import facets
import otherFacets
import shouldEqual
import kotlin.test.Test


class TestRefinement {
    private class MockView : RefinementView<Facet> {

        lateinit var click: (Facet) -> Unit
        var data = listOf<Facet>()
        var dataSelected = listOf<Facet>()

        override fun setRefinements(refinements: List<Facet>) {
            data = refinements
        }

        override fun setSelected(refinements: List<Facet>) {
            dataSelected = refinements
        }

        override fun onClickRefinement(onClick: (Facet) -> Unit) {
            click = { refinement: Facet ->
                onClick(refinement)
            }
        }
    }

    private fun getViews() = listOf(MockView(), MockView(), MockView())

    private fun setup(model: RefinementModel<Facet>, views: List<MockView>, searcher: MockSearcher) {
        model.connectViews(views)
        model.connectSearcher(searcher)
        model.refinements = facets
        facets.forEach {
            views.first().click(it)
        }
        model.refinements shouldEqual facets
        views.forEach {
            it.data shouldEqual facets
        }
        searcher.count shouldEqual facets.size
    }

    private fun multiple(model: RefinementModel<Facet>, views: List<MockView>, searcher: MockSearcher) {
        setup(model, views, searcher)
        views.forEach {
            it.data shouldEqual facets
        }
        model.refinements = otherFacets
        model.selected shouldEqual otherFacets
        views.forEach {
            it.data shouldEqual otherFacets
            it.dataSelected shouldEqual otherFacets
        }
    }

    private fun single(model: RefinementModel<Facet>, views: List<MockView>, searcher: MockSearcher) {
        setup(model, views, searcher)
        model.selected shouldEqual listOf(facets.last())
        views.forEach {
            it.dataSelected shouldEqual listOf(facets.last())
        }
        model.refinements = otherFacets
        model.selected shouldEqual listOf()
        views.forEach {
            it.data shouldEqual otherFacets
            it.dataSelected shouldEqual listOf()
        }
    }

    @Test
    fun disjunctive() {
        val model = RefinementModel<Facet>(RefinementModel.Mode.Disjunctive)
        val views = getViews()
        val searcher = MockSearcher()

        multiple(model, views, searcher)
    }

    @Test
    fun conjunctiveMultipleChoice() {
        val model = RefinementModel<Facet>(RefinementModel.Mode.ConjunctiveMultipleChoice)
        val views = getViews()
        val searcher = MockSearcher()

        multiple(model, views, searcher)
    }

    @Test
    fun conjunctiveSingleChoice() {
        val model = RefinementModel<Facet>(RefinementModel.Mode.ConjunctiveSingleChoice)
        val views = getViews()
        val searcher = MockSearcher()

        single(model, views, searcher)
    }
}
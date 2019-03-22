package refinement

import Facet
import MockSearcher
import facets
import kotlinx.coroutines.CancellationException
import otherFacets
import shouldContain
import shouldEqual
import kotlin.test.Test
import kotlin.test.assertFailsWith


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

        override fun setOnClickRefinement(onClick: (Facet) -> Unit) {
            click = { onClick(it) }
        }
    }

    private fun getViews() = listOf(MockView(), MockView(), MockView())

    private fun setup(model: RefinementListViewModel<Facet>, views: List<MockView>, searcher: MockSearcher) {
        model.connectViews(views)
        model.connectSearcher(searcher)
        model.refinements = facets
        facets.forEach { views.first().click(it) }
        model.refinements shouldEqual facets
        views.forEach { it.data shouldEqual facets }
        searcher.count shouldEqual facets.size
    }

    private fun multiple(
        model: RefinementListViewModel<Facet>,
        views: List<MockView> = getViews(),
        searcher: MockSearcher = MockSearcher()
    ) {
        setup(model, views, searcher)
        views.forEach { it.data shouldEqual facets }
        model.refinements = otherFacets
        model.selected shouldEqual otherFacets
        views.forEach {
            it.data shouldEqual otherFacets
            it.dataSelected shouldEqual otherFacets
        }
    }

    private fun single(model: RefinementListViewModel<Facet>,
                       views: List<MockView> = getViews(),
                       searcher: MockSearcher = MockSearcher()) {
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
    fun `selected refinement stays selected when new refinements still contain it`() {
        val model = RefinementListViewModel<Facet>()
        val views = getViews()
        val searcher = MockSearcher()
        setup(model, views, searcher)

        model.refinements = facets
        views.first().click(facets[0])
        model.refinements = otherFacets
        views.forEach {
            it.dataSelected shouldContain facets[0]
        }
    }

    @Test
    fun `selected listener gets updated on new refinement if the selection is removed`() {
        val model = RefinementListViewModel<Facet>()
        model.refinements = facets
        model.onSelectedRefinement(facets[2])
        assertFailsWith<CancellationException> {
            model.selectionListeners += {
                throw CancellationException("The selectedlistener was called as selection becomes obsolete")
            }
            model.refinements = otherFacets
        }
    }

    @Test
    fun disjunctive() {
        multiple(RefinementListViewModel(RefinementListViewModel.Mode.MultipleChoices))
    }

    @Test
    fun singleChoice() {
        single(RefinementListViewModel(RefinementListViewModel.Mode.SingleChoice))
    }
}
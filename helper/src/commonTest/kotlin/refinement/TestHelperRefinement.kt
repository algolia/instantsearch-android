package refinement

import blocking
import com.algolia.search.filter.FilterFacet
import com.algolia.search.filter.setFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.Query
import indexA
import searcher.SearcherForFacetValue
import searcher.SearcherSingleIndex
import shouldBeEmpty
import shouldBeNull
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestHelperRefinement {

    private class MockView : RefinementView<Facet> {

        lateinit var click: (Facet, Boolean) -> Unit
        var data = listOf<Facet>()
        var dataSelected = listOf<Facet>()

        override fun setRefinements(refinements: List<Facet>) {
            data = refinements
        }

        override fun setSelected(refinements: List<Facet>) {
            dataSelected = refinements
        }

        override fun setOnClickRefinement(onClick: (Facet, Boolean) -> Unit) {
            click = { refinement: Facet, isActive: Boolean ->
                onClick(refinement, isActive)
            }
        }
    }

    @Test
    fun withSearcherSingleIndexConjunctive() {
        blocking {
            val attribute = Attribute("brand")
            val query = Query().apply { setFacets(attribute) }
            val searcher = SearcherSingleIndex(indexA, query)
            val model = RefinementListViewModel<Facet>()
            val filterState = SearchFilterState()
            val view = MockView()
            model.connectView(view)
            model.connectSearcherSingleIndex(searcher, attribute)
            filterState.connectRefinementModel(model, attribute)
            filterState.connectSearcherSingleIndex(searcher)

            searcher.search()
            searcher.completed?.await()
            model.data.size shouldEqual 2
            filterState.filterBuilder.get().shouldBeEmpty()

            view.click(model.data.first(), true)
            searcher.completed?.await()
            filterState.filterBuilder.get() shouldEqual setOf(FilterFacet(attribute, model.data.first().name))
            model.data.size shouldEqual 1
        }
    }

    @Test
    fun withSearcherSingleIndexDisjunctive() {
        blocking {
            val attribute = Attribute("brand")
            val query = Query().apply { setFacets(attribute) }
            val searcher = SearcherSingleIndex(indexA, query)
            val model = RefinementListViewModel<Facet>(RefinementListViewModel.Mode.MultipleChoices)
            val view = MockView()
            val view2 = MockView()
            model.connectViews(listOf(view, view2))
            model.connectSearcherSingleIndex(searcher, attribute)

            searcher.search()
            searcher.completed?.await()
            model.data.size shouldEqual 2
            query.filters.shouldBeNull()

            view.click(model.data.first(), true)
            view2.click(model.data.last(), true)
            searcher.completed?.await()
            query.filters.shouldNotBeNull()
            model.data.size shouldEqual 2
        }
    }

    @Test
    fun withSearcherForFacetValue() {
        blocking {
            val attribute = Attribute("name")
            val searcher = SearcherForFacetValue(indexA, attribute)

            val model = RefinementListViewModel<Facet>()
            val filterState = SearchFilterState()
            val view = MockView()

            model.connectView(view)
            model.connectSearcherForFacetValue(searcher, attribute)
            filterState.connectSearcherForFacetValue(searcher)
            filterState.connectRefinementModel(model, attribute)

            searcher.search()
            searcher.completed?.await()
            model.data.size shouldEqual 2

            view.click(model.data.first(), true)
            searcher.completed?.await()
            filterState.filterBuilder.get() shouldEqual setOf(FilterFacet(attribute, model.data.first().name))
            model.data.size shouldEqual 2
        }
    }
}

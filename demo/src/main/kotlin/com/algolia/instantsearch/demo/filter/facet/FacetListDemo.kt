package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion.*
import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_facet_list.*
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*


class FacetListDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotions = Attribute("promotions")
    private val category = Attribute("category")
    private val groupColor = groupAnd(color)
    private val groupPromotions = groupAnd(promotions)
    private val groupCategory = groupOr(category)
    private val filterState = filterState { group(groupColor) { facet(color, "green") } }
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list)

        searcher.connectFilterState(filterState)

        val colorAViewModel = FacetListViewModel(selectionMode = SelectionMode.Single)
        val colorAPresenter = FacetListPresenterImpl(listOf(IsRefined, AlphabeticalAscending), limit = 3)
        val colorAAdapter = FacetListAdapter()

        colorAViewModel.connectFilterState(filterState, color, groupColor)
        colorAViewModel.connectSearcher(searcher, color)
        colorAViewModel.connectView(colorAAdapter, colorAPresenter)

        val promotionViewModel = FacetListViewModel()
        val promotionPresenter = FacetListPresenterImpl(listOf(CountDescending))
        val promotionAdapter = FacetListAdapter()

        promotionViewModel.connectFilterState(filterState, promotions, groupPromotions)
        promotionViewModel.connectSearcher(searcher, promotions)
        promotionViewModel.connectView(promotionAdapter, promotionPresenter)

        val categoryViewModel = FacetListViewModel()
        val categoryPresenter = FacetListPresenterImpl(listOf(CountDescending, AlphabeticalAscending))
        val categoryAdapter = FacetListAdapter()

        categoryViewModel.connectFilterState(filterState, category, groupCategory)
        categoryViewModel.connectSearcher(searcher, category)
        categoryViewModel.connectView(categoryAdapter, categoryPresenter)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, colorAAdapter)
        configureRecyclerView(listTopRight, categoryAdapter)
        configureRecyclerView(listBottomLeft, promotionAdapter)
        configureTitle(titleTopLeft, formatTitle(colorAPresenter, groupColor))
        configureTitle(titleTopRight, formatTitle(categoryPresenter, groupCategory))
        configureTitle(titleBottomLeft, formatTitle(promotionPresenter, groupPromotions))
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, promotions, category)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }

    private fun FacetSortCriterion.format(): String {
        return when (this) {
            IsRefined -> name
            CountAscending -> "CountAsc"
            CountDescending -> "CountDesc"
            AlphabeticalAscending -> "AlphaAsc"
            AlphabeticalDescending -> "AlphaDesc"
        }
    }

    private fun formatTitle(presenter: FacetListPresenterImpl, filterGroupID: FilterGroupID): String {

        val criteria = presenter.sortBy.joinToString("-") { it.format() }
        val operator = when (filterGroupID.operator) {
            FilterOperator.And -> "And"
            FilterOperator.Or -> "Or"
        }

        return "$operator, $criteria, l=${presenter.limit}"
    }
}
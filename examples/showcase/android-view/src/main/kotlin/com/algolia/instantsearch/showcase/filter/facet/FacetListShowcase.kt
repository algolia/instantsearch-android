package com.algolia.instantsearch.showcase.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.*
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.filter.state.*
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.IncludeListBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseFacetListBinding
import com.algolia.search.model.Attribute

class FacetListShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotions = Attribute("promotions")
    private val category = Attribute("category")
    private val groupColor = groupAnd(color)
    private val groupPromotions = groupAnd(promotions)
    private val groupCategory = groupOr(category)
    private val filterState = filterState { group(groupColor) { facet(color, "green") } }
    private val searcher =  HitsSearcher(client, stubIndexName)
    private val facetListColor = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = color,
        selectionMode = SelectionMode.Single,
        groupID = groupColor
    )
    private val facetListPromotions = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = promotions,
        selectionMode = SelectionMode.Multiple,
        groupID = groupPromotions
    )
    private val facetListCategory = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = category,
        selectionMode = SelectionMode.Multiple,
        groupID = groupCategory
    )
    private val connection = ConnectionHandler(
        facetListColor,
        facetListPromotions,
        facetListCategory,
        searcher.connectFilterState(filterState)
    )
    private val colorPresenter = FacetListPresenterImpl(listOf(IsRefined, AlphabeticalAscending), limit = 3)
    private val colorAdapter = FacetListAdapter(FacetListViewHolderImpl.Factory)
    private val promotionPresenter = FacetListPresenterImpl(listOf(CountDescending))
    private val promotionAdapter = FacetListAdapter(FacetListViewHolderImpl.Factory)
    private val categoryPresenter = FacetListPresenterImpl(listOf(CountDescending, AlphabeticalAscending))
    private val categoryAdapter = FacetListAdapter(FacetListViewHolderImpl.Factory)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseFacetListBinding.inflate(layoutInflater)
        val listBinding = IncludeListBinding.bind(binding.list.root)
        val headerBinding = HeaderFilterBinding.bind(listBinding.headerFilter.root)
        setContentView(binding.root)

        connection += facetListColor.connectView(colorAdapter, colorPresenter)
        connection += facetListPromotions.connectView(promotionAdapter, promotionPresenter)
        connection += facetListCategory.connectView(categoryAdapter, categoryPresenter)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listBinding.listTopLeft, colorAdapter)
        configureRecyclerView(listBinding.listTopRight, categoryAdapter)
        configureRecyclerView(listBinding.listBottomLeft, promotionAdapter)
        configureTitle(listBinding.titleTopLeft, formatTitle(colorPresenter, groupColor))
        configureTitle(listBinding.titleTopRight, formatTitle(categoryPresenter, groupCategory))
        configureTitle(listBinding.titleBottomLeft, formatTitle(promotionPresenter, groupPromotions))
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, color, promotions, category)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
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
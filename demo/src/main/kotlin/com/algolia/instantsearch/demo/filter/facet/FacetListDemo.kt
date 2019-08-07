package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion.*
import com.algolia.instantsearch.helper.filter.facet.connectView
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
        setContentView(R.layout.demo_facet_list)

        connection += facetListColor.connectView(colorAdapter, colorPresenter)
        connection += facetListPromotions.connectView(promotionAdapter, promotionPresenter)
        connection += facetListCategory.connectView(categoryAdapter, categoryPresenter)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, colorAdapter)
        configureRecyclerView(listTopRight, categoryAdapter)
        configureRecyclerView(listBottomLeft, promotionAdapter)
        configureTitle(titleTopLeft, formatTitle(colorPresenter, groupColor))
        configureTitle(titleTopRight, formatTitle(categoryPresenter, groupCategory))
        configureTitle(titleBottomLeft, formatTitle(promotionPresenter, groupPromotions))
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, promotions, category)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
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
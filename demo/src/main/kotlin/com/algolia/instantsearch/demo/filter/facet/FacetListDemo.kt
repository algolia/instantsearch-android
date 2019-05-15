package com.algolia.instantsearch.demo.filter.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion.*
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_facet_list.*
import kotlinx.android.synthetic.main.header_filter.*


class FacetListDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotions = Attribute("promotions")
    private val category = Attribute("category")
    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            promotions.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private val groupIDColor = FilterGroupID.And(color)
    private val groupIDPromotions = FilterGroupID.And(promotions)
    private val groupIDCategory = FilterGroupID.Or(category)
    private val index = client.initIndex(IndexName("mobile_demo_facet_list"))
    private val filterState = FilterState(
        facetGroups = mutableMapOf(
            FilterGroupID.And(color.raw) to setOf(Filter.Facet(color, "green"))
        )
    )
    private val searcher = SearcherSingleIndex(index, filterState = filterState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list)

        val colorAViewModel = FacetListViewModel()
        val colorAPresenter = FacetListPresenter(listOf(IsRefined, AlphabeticalAscending), 5)
        val colorAAdapter = FacetListAdapter()

        colorAViewModel.connectFilterState(color, filterState, groupIDColor)
        colorAViewModel.connectSearcher(color, searcher)
        colorAViewModel.connectView(colorAAdapter, colorAPresenter)

        val colorBViewModel = FacetListViewModel(selectionMode = SelectionMode.Single)
        val colorBPresenter = FacetListPresenter(listOf(AlphabeticalDescending), 3)
        val colorBAdapter = FacetListAdapter()

        colorBViewModel.connectFilterState(color, filterState, groupIDColor)
        colorBViewModel.connectSearcher(color, searcher)
        colorBViewModel.connectView(colorBAdapter, colorBPresenter)

        val promotionViewModel = FacetListViewModel()
        val promotionPresenter = FacetListPresenter(listOf(CountDescending), 5)
        val promotionAdapter = FacetListAdapter()

        promotionViewModel.connectFilterState(promotions, filterState, groupIDPromotions)
        promotionViewModel.connectSearcher(promotions, searcher)
        promotionViewModel.connectView(promotionAdapter, promotionPresenter)

        val categoryViewModel = FacetListViewModel()
        val categoryPresenter = FacetListPresenter(listOf(CountDescending, AlphabeticalAscending), 5)
        val categoryAdapter = FacetListAdapter()

        categoryViewModel.connectFilterState(category, filterState, groupIDCategory)
        categoryViewModel.connectSearcher(category, searcher)
        categoryViewModel.connectView(categoryAdapter, categoryPresenter)

        configureRecyclerView(listTopLeft, colorAAdapter)
        configureRecyclerView(listTopRight, colorBAdapter)
        configureRecyclerView(listBottomLeft, promotionAdapter)
        configureRecyclerView(listBottomRight, categoryAdapter)

        titleTopLeft.text = formatTitle(colorAPresenter, groupIDColor)
        titleTopRight.text = formatTitle(colorBPresenter, groupIDColor)
        titleBottomLeft.text = formatTitle(promotionPresenter, groupIDPromotions)
        titleBottomRight.text = formatTitle(categoryPresenter, groupIDCategory)

        onChangeThenUpdateFiltersText(filterState, colors, filtersTextView)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher)

        searcher.search()
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

    private fun formatTitle(presenter: FacetListPresenter, filterGroupID: FilterGroupID): String {

        val criteria = presenter.sortBy.joinToString("-") { it.format() }
        val operator = when (filterGroupID) {
            is FilterGroupID.And -> "And"
            is FilterGroupID.Or -> "Or"
        }

        return "$operator, $criteria, l=${presenter.limit}"
    }
}
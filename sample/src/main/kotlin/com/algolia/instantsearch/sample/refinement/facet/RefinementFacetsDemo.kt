package com.algolia.instantsearch.sample.refinement.facet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.FilterState
import filter.toFilterGroups
import highlight
import kotlinx.android.synthetic.main.refinement_facets_demo.*
import refinement.facet.*
import refinement.facet.RefinementFacetSortCriterion.*
import search.SearcherSingleIndex
import selection.list.SelectionMode


class RefinementFacetsDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotions = Attribute("promotions")
    private val category = Attribute("category")

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
        )
    )
    private val index = client.initIndex(IndexName("mobile_demo_refinement_facets"))
    private val filterState = FilterState(
        facetGroups = mutableMapOf(
            FilterGroupID.And(color.raw) to setOf(Filter.Facet(color, "green"))
        )
    )
    private val searcher = SearcherSingleIndex(index, filterState = filterState)

    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            promotions.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private val groupIDColor = FilterGroupID.And(color)
    private val groupIDPromotions = FilterGroupID.And(promotions)
    private val groupIDCategory = FilterGroupID.Or(category)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_facets_demo)

        val colorAViewModel = RefinementFacetsViewModel()
        val colorAPresenter = RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)
        val colorAAdapter = RefinementFacetsAdapter()

        colorAViewModel.connectSearcher(color, searcher, groupIDColor)
        colorAViewModel.connectView(colorAAdapter, colorAPresenter)

        val colorBViewModel = RefinementFacetsViewModel(selectionMode = SelectionMode.Single)
        val colorBPresenter = RefinementFacetsPresenter(listOf(AlphabeticalDescending), 3)
        val colorBAdapter = RefinementFacetsAdapter()

        colorBViewModel.connectSearcher(color, searcher, groupIDColor)
        colorBViewModel.connectView(colorBAdapter, colorBPresenter)

        val promotionViewModel = RefinementFacetsViewModel(selectionMode = SelectionMode.Multiple)
        val promotionPresenter = RefinementFacetsPresenter(listOf(CountDescending), 5)
        val promotionAdapter = RefinementFacetsAdapter()

        promotionViewModel.connectSearcher(promotions, searcher, groupIDPromotions)
        promotionViewModel.connectView(promotionAdapter, promotionPresenter)

        val categoryViewModel = RefinementFacetsViewModel(selectionMode = SelectionMode.Multiple)
        val categoryPresenter = RefinementFacetsPresenter(listOf(CountDescending, AlphabeticalAscending), 5)
        val categoryAdapter = RefinementFacetsAdapter()

        categoryViewModel.connectSearcher(category, searcher, groupIDCategory)
        categoryViewModel.connectView(categoryAdapter, categoryPresenter)

        configureRecyclerView(listTopLeft, colorAAdapter)
        configureRecyclerView(listTopRight, colorBAdapter)
        configureRecyclerView(listBottomLeft, promotionAdapter)
        configureRecyclerView(listBottomRight, categoryAdapter)

        titleTopLeft.text = formatTitle(colorAPresenter, groupIDColor)
        titleTopRight.text = formatTitle(colorBPresenter, groupIDColor)
        titleBottomLeft.text = formatTitle(promotionPresenter, groupIDPromotions)
        titleBottomRight.text = formatTitle(categoryPresenter, groupIDCategory)

        onChangeThenUpdateFiltersText(filtersTextView)
        onErrorThenUpdateFiltersText(filtersTextView)
        onClearAllThenClearFilters(filtersClearAll)
        updateFiltersTextFromState(filtersTextView)

        searcher.search()
    }


    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }

    private fun onChangeThenUpdateFiltersText(filtersTextView: TextView) {
        searcher.filterState.onChange += {
            filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
        }
    }

    private fun updateFiltersTextFromState(filtersTextView: TextView) {
        filtersTextView.text = searcher.filterState.toFilterGroups().highlight(colors = colors)
    }

    private fun onClearAllThenClearFilters(filtersClearAll: View) {
        filtersClearAll.setOnClickListener {
            searcher.filterState.notify { clear() }
        }
    }

    private fun onErrorThenUpdateFiltersText(filtersTextView: TextView) {
        searcher.errorListeners += {
            filtersTextView.text = it.localizedMessage
        }
    }

    private fun configureRecyclerView(
        recyclerView: View,
        view: RefinementFacetsAdapter
    ) {
        (recyclerView as RecyclerView).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = view
            it.itemAnimator = null
        }
    }

    private fun RefinementFacetSortCriterion.format(): String {
        return when (this) {
            IsRefined -> name
            CountAscending -> "CountAsc"
            CountDescending -> "CountDesc"
            AlphabeticalAscending -> "AlphaAsc"
            AlphabeticalDescending -> "AlphaDesc"
        }
    }

    private fun formatTitle(presenter: RefinementFacetsPresenter, filterGroupID: FilterGroupID): String {
        val criteria = presenter.sortBy.joinToString("-") { it.format() }
        val refinementMode = when (filterGroupID) {
            is FilterGroupID.And -> "And"
            is FilterGroupID.Or -> "Or"
        }

        return "$refinementMode, $criteria, l=${presenter.limit}"
    }
}
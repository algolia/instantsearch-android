package com.algolia.instantsearch.sample.refinement

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
import kotlinx.android.synthetic.main.activity_refinement_facets.*
import refinement.*
import refinement.FacetSortCriterion.*
import search.SearcherSingleIndex
import selection.SelectionMode


class RefinementFacetsActivity : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotion = Attribute("promotion")
    private val category = Attribute("category")

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
        )
    )
    private val index = client.initIndex(IndexName("mobile_demo_refinement_facet"))
    private val filterState = FilterState(
        facetGroups = mutableMapOf(
            FilterGroupID.And(color.raw) to setOf(Filter.Facet(color, "green"))
        )
    )
    private val searcher = SearcherSingleIndex(index, filterState = filterState)

    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            promotion.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refinement_facets)

        val colorAViewModel = RefinementFacetsViewModel()
        val colorAPresenter = RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)
        val colorAAdapter = RefinementFacetsAdapter()

        colorAViewModel.connectSearcher(color, searcher, RefinementOperator.And)
        colorAViewModel.connectView(colorAAdapter, colorAPresenter)

        val colorBViewModel = RefinementFacetsViewModel(SelectionMode.Single)
        val colorBPresenter = RefinementFacetsPresenter(listOf(AlphabeticalDescending), 3)
        val colorBAdapter = RefinementFacetsAdapter()

        colorBViewModel.connectSearcher(color, searcher, RefinementOperator.And)
        colorBViewModel.connectView(colorBAdapter, colorBPresenter)

        val promotionViewModel = RefinementFacetsViewModel(SelectionMode.Multiple)
        val promotionPresenter = RefinementFacetsPresenter(listOf(CountDescending), 5)
        val promotionAdapter = RefinementFacetsAdapter()

        promotionViewModel.connectSearcher(promotion, searcher, RefinementOperator.And)
        promotionViewModel.connectView(promotionAdapter, promotionPresenter)

        val categoryViewModel = RefinementFacetsViewModel(SelectionMode.Multiple)
        val categoryPresenter = RefinementFacetsPresenter(listOf(CountDescending, AlphabeticalAscending), 5)
        val categoryAdapter = RefinementFacetsAdapter()

        categoryViewModel.connectSearcher(category, searcher, RefinementOperator.Or)
        categoryViewModel.connectView(categoryAdapter, categoryPresenter)

        configureRecyclerView(listTopLeft, colorAAdapter)
        configureRecyclerView(listTopRight, colorBAdapter)
        configureRecyclerView(listBottomLeft, promotionAdapter)
        configureRecyclerView(listBottomRight, categoryAdapter)

        titleTopLeft.text = formatTitle(colorAPresenter, RefinementOperator.And)
        titleTopRight.text = formatTitle(colorBPresenter, RefinementOperator.And)
        titleBottomLeft.text = formatTitle(promotionPresenter, RefinementOperator.And)
        titleBottomRight.text = formatTitle(categoryPresenter, RefinementOperator.Or)

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

    private fun FacetSortCriterion.format(): String {
        return when (this) {
            IsRefined -> name
            CountAscending -> "CountAsc"
            CountDescending -> "CountDesc"
            AlphabeticalAscending -> "AlphaAsc"
            AlphabeticalDescending -> "AlphaDesc"
        }
    }

    private fun formatTitle(presenter: RefinementFacetsPresenter, refinementMode: RefinementOperator): String {
        val criteria = presenter.sortBy.joinToString("-") { it.format() }

        return "$refinementMode, $criteria, l=${presenter.limit}"
    }
}
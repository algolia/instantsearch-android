package com.algolia.instantsearch.sample.refinement.facet

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.dsl.facets
import com.algolia.search.dsl.query
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.FilterState
import filter.toFilterGroups
import highlight
import kotlinx.android.synthetic.main.refinement_activity.*
import refinement.facet.*
import refinement.RefinementOperator
import refinement.facet.FacetSortCriterion.*
import search.SearcherSingleIndex
import selection.SelectionMode


class RefinementFacetActivity : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotion = Attribute("promotion")
    private val category = Attribute("category")

    private val query = query {
        facets {
            +color
            +promotion
            +category
        }
    }

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
        )
    )

    private val index = client.initIndex(IndexName("mobile_demo_refinement_facet"))
    private val filterState = FilterState(
        facets = mutableMapOf(
            FilterGroupID.And(color.raw) to setOf(Filter.Facet(color, "green"))
        )
    )
    private val searcher = SearcherSingleIndex(index, query, filterState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_activity)

        val viewModelA = RefinementFacetsViewModel(SelectionMode.Single)
        val viewA = SelectionListAdapter()
        val presenterA = RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)

        viewModelA.connectSearcher(color, searcher, RefinementOperator.And)
        viewModelA.connectView(viewA, presenterA)

        val viewB = SelectionListAdapter()
        val presenterB = RefinementFacetsPresenter(listOf(AlphabeticalDescending), 3)

        viewModelA.connectView(viewB, presenterB)

        val viewModelC = RefinementFacetsViewModel(SelectionMode.Multiple)
        val viewC = SelectionListAdapter()
        val presenterC = RefinementFacetsPresenter(listOf(CountDescending), 5)

        viewModelC.connectSearcher(promotion, searcher, RefinementOperator.And)
        viewModelC.connectView(viewC, presenterC)

        val viewModelD = RefinementFacetsViewModel(SelectionMode.Multiple)
        val viewD = SelectionListAdapter()
        val presenterD = RefinementFacetsPresenter(listOf(CountDescending, AlphabeticalAscending), 5)

        viewModelD.connectSearcher(category, searcher, RefinementOperator.Or)
        viewModelD.connectView(viewD, presenterD)

        configureRecyclerView(listA, viewA)
        configureRecyclerView(listB, viewB)
        configureRecyclerView(listC, viewC)
        configureRecyclerView(listD, viewD)
        listATitle.text = formatTitle(presenterA, RefinementOperator.And)
        listBTitle.text = formatTitle(presenterB, RefinementOperator.And)
        listCTitle.text = formatTitle(presenterC, RefinementOperator.And)
        listDTitle.text = formatTitle(presenterD, RefinementOperator.Or)

        searcher.filterState.onStateChanged += {
            filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
        }

        filtersClearAll.setOnClickListener {
            searcher.filterState.notify {
                clear()
            }
        }
        filtersTextView.text = searcher.filterState.toFilterGroups().highlight(colors = colors)
        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }

    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            promotion.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private fun configureRecyclerView(
        recyclerView: View,
        view: SelectionListAdapter
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
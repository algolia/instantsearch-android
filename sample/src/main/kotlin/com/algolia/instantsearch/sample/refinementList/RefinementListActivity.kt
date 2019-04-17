package com.algolia.instantsearch.sample.refinementList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import filter.FilterState
import filter.toFilterGroups
import highlight
import kotlinx.android.synthetic.main.refinement_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import refinement.*
import search.GroupID
import search.SearcherSingleIndex


class RefinementListActivity : AppCompatActivity(), CoroutineScope {


    private val configuration = RefinementListConfiguration()
    private val index = configuration.client.initIndex(IndexName("mobile_demo_refinement"))
    private val filterState = FilterState(
        facets = mutableMapOf(
            GroupID.And(configuration.color.raw) to setOf(Filter.Facet(configuration.color, "green"))
        )
    )
    private val searcher = SearcherSingleIndex(index, configuration.query, filterState)

    override val coroutineContext = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_activity)

        val viewModelA = RefinementFacetsViewModel(SelectionMode.SingleChoice)
        val viewA = RefinementListAdapter()
        val presenterA = RefinementFacetsPresenter(listOf(SortCriterion.IsRefined, SortCriterion.AlphabeticalAsc), 5)

        viewModelA.connect(configuration.color, searcher, RefinementMode.And)
        viewModelA.connect(presenterA)
        viewModelA.connect(viewA)
        presenterA.connect(viewA)

        val viewB = RefinementListAdapter()
        val presenterB = RefinementFacetsPresenter(listOf(SortCriterion.AlphabeticalDesc), 3)

        viewModelA.connect(configuration.color, searcher, RefinementMode.And)
        viewModelA.connect(presenterB)
        viewModelA.connect(viewB)
        presenterB.connect(viewB)

        val viewModelC = RefinementFacetsViewModel(SelectionMode.MultipleChoice)
        val viewC = RefinementListAdapter()
        val presenterC = RefinementFacetsPresenter(listOf(SortCriterion.CountDesc), 5)

        viewModelC.connect(configuration.promotion, searcher, RefinementMode.And)
        viewModelC.connect(presenterC)
        viewModelC.connect(viewC)
        presenterC.connect(viewC)

        val viewModelD = RefinementFacetsViewModel(SelectionMode.MultipleChoice)
        val viewD = RefinementListAdapter()
        val presenterD = RefinementFacetsPresenter(listOf(SortCriterion.CountDesc, SortCriterion.AlphabeticalAsc), 5)

        viewModelD.connect(configuration.category, searcher, RefinementMode.Or)
        viewModelD.connect(presenterD)
        viewModelD.connect(viewD)
        presenterD.connect(viewD)

        configureRecyclerView(listA, viewA)
        configureRecyclerView(listB, viewB)
        configureRecyclerView(listC, viewC)
        configureRecyclerView(listD, viewD)
        listATitle.text = formatTitle(presenterA, RefinementMode.And)
        listBTitle.text = formatTitle(presenterB, RefinementMode.And)
        listCTitle.text = formatTitle(presenterC, RefinementMode.And)
        listDTitle.text = formatTitle(presenterD, RefinementMode.Or)

        searcher.filterState.listeners += {
            filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
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
            configuration.color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            configuration.promotion.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            configuration.category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private fun configureRecyclerView(
        recyclerView: View,
        view: RefinementListAdapter
    ) {
        (recyclerView as RecyclerView).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = view
            it.itemAnimator = null
        }
    }

    private fun SortCriterion.format(): String {
        return when (this) {
            SortCriterion.IsRefined -> name
            SortCriterion.CountAsc -> name
            SortCriterion.CountDesc -> name
            SortCriterion.AlphabeticalAsc -> "AlphaAsc"
            SortCriterion.AlphabeticalDesc -> "Alphadesc"
        }
    }

    private fun formatTitle(presenter: RefinementFacetsPresenter, refinementMode: RefinementMode): String {
        val criteria = presenter.sortCriteria.joinToString("-") { it.format() }

        return "$refinementMode, $criteria, l=${presenter.limit}"
    }
}
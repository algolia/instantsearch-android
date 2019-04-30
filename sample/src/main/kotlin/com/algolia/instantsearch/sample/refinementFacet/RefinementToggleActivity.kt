package com.algolia.instantsearch.sample.refinementFacet

import refinement.facet.list.FacetSortCriterion.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.refinement.facet.RefinementFacetsAdapter
import com.algolia.instantsearch.sample.refinementToggle.CheckBoxFacetRefinement
// import com.algolia.instantsearch.sample.R
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
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.FilterState
import filter.toFilterGroups
import highlight
import kotlinx.android.synthetic.main.refinement_toggle_activity.*
import refinement.RefinementOperator
import refinement.facet.RefinementFacetViewModel
import refinement.facet.list.RefinementFacetsViewModel
import refinement.facet.connectSearcher
import refinement.facet.connectView
import refinement.facet.list.connectSearcher
import refinement.facet.list.connectView
import refinement.facet.list.FacetSortCriterion
import refinement.facet.list.RefinementFacetsPresenter
import search.SearcherSingleIndex
import selection.SelectionMode


class RefinementToggleActivity : AppCompatActivity() {

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

    private val index = client.initIndex(IndexName("mobile_demo_refinement_facet")) //TODO: Separate index
    private val searcher = SearcherSingleIndex(
        index, query, FilterState(
            facetGroups = mutableMapOf(FilterGroupID.Or(color) to setOf(Filter.Facet(color, "green")))
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_toggle_activity)


        // CheckBox | color: red
        val viewModelA = RefinementFacetViewModel()
        val facetA = Facet("red", 0)
        val viewRed = CheckBoxFacetRefinement(toggleA)

        // CheckBox | color: green
        val viewModelB = RefinementFacetViewModel()
        val facetB = Facet("green", 0)
        val viewGreen = CheckBoxFacetRefinement(toggleB)

        viewModelA.connectSearcher(color, searcher, facetA)
        viewModelA.connectView(viewRed)
        viewModelB.connectSearcher(color, searcher, facetB)
        viewModelB.connectView(viewGreen)

        // Chip | size: 42
        // Toggle | popular: true/false

        // RefinementList | color: OR | CountDesc,AlphaAsc | limit=5


        // region FiltersText / FiltersClearAll / Trigger search
        //TODO: Factorize with RefinementFacetActivity
        searcher.filterState.onStateChanged += {
            filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
        }

        filtersClearAll.setOnClickListener {
            searcher.filterState.notify {
                clear()
            }
        }
        filtersTextView.text = searcher.filterState.toFilterGroups().highlight(colors = colors)

        val viewModelList = RefinementFacetsViewModel(SelectionMode.Single)
        val viewList = RefinementFacetsAdapter()
        val presenterList = RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)

        viewModelList.connectSearcher(color, searcher, RefinementOperator.Or)
        viewModelList.connectView(viewList, presenterList)
//

        searcher.search()
        // endregion

        // region FIXME OLD CODE, to be migrated before this region
//        val viewModelA = RefinementFacetsViewModel(SelectionMode.Single)
//        val viewA = SelectionListAdapter()
//        val presenterA = RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)
//
//        viewModelA.connect(color, searcher, RefinementOperator.And)
//        viewModelA.connect(viewA, presenterA)
//
//        val viewB = SelectionListAdapter()
//        val presenterB = RefinementFacetsPresenter(listOf(AlphabeticalDescending), 3)
//
//        viewModelA.connect(color, searcher, RefinementOperator.And)
//        viewModelA.connect(viewB, presenterB)
//
//        val viewModelC = RefinementFacetsViewModel(SelectionMode.Multiple)
//        val viewC = SelectionListAdapter()
//        val presenterC = RefinementFacetsPresenter(listOf(CountDescending), 5)
//
//        viewModelC.connect(promotion, searcher, RefinementOperator.And)
//        viewModelC.connect(viewC, presenterC)
//
//        val viewModelD = RefinementFacetsViewModel(SelectionMode.Multiple)
//        val viewD = SelectionListAdapter()
//        val presenterD = RefinementFacetsPresenter(listOf(CountDescending, AlphabeticalAscending), 5)
//
//        viewModelD.connect(category, searcher, RefinementOperator.Or)
//        viewModelD.connect(viewD, presenterD)

//        configureRecyclerView(listA, viewA)
//        configureRecyclerView(listB, viewB)
//        configureRecyclerView(listC, viewC)
//        configureRecyclerView(listD, viewD)
//        listATitle.text = formatTitle(presenterA, RefinementOperator.And)
//        listBTitle.text = formatTitle(presenterB, RefinementOperator.And)
//        listCTitle.text = formatTitle(presenterC, RefinementOperator.And)
//        listDTitle.text = formatTitle(presenterD, RefinementOperator.Or)

        // endregion
    }

    fun Context.toast(text: String) {
        Log.e("TOAST", text)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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
            FacetSortCriterion.IsRefined -> name
            FacetSortCriterion.CountAscending -> "CountAsc"
            FacetSortCriterion.CountDescending -> "CountDesc"
            FacetSortCriterion.AlphabeticalAscending -> "AlphaAsc"
            FacetSortCriterion.AlphabeticalDescending -> "AlphaDesc"
        }
    }

    private fun formatTitle(presenter: RefinementFacetsPresenter, refinementMode: RefinementOperator): String {
        val criteria = presenter.sortBy.joinToString("-") { it.format() }

        return "$refinementMode, $criteria, l=${presenter.limit}"
    }
}



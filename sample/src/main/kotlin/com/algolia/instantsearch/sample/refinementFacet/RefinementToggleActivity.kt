package com.algolia.instantsearch.sample.refinementFacet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.instantsearch.sample.refinement.facet.RefinementActivity
import com.algolia.instantsearch.sample.refinement.facet.RefinementFacetsAdapter
import com.algolia.instantsearch.sample.refinementToggle.CheckBoxFacetRefinement
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.refinement_toggle_activity.*
import refinement.RefinementOperator
import refinement.facet.RefinementFilterViewModel
import refinement.facet.connectSearcher
import refinement.facet.connectView
import refinement.facet.list.FacetSortCriterion.AlphabeticalAscending
import refinement.facet.list.FacetSortCriterion.IsRefined
import refinement.facet.list.RefinementFacetsPresenter
import refinement.facet.list.RefinementFacetsViewModel
import refinement.facet.list.connectSearcher
import refinement.facet.list.connectView
import selection.SelectionMode


class RefinementToggleActivity : RefinementActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_toggle_activity)


        // CheckBox | color: red
        val viewModelA = RefinementFilterViewModel(Filter.Facet(color, "red"))
        val viewRed = CheckBoxFacetRefinement(toggleA)

        // CheckBox | color: green
        val viewModelB = RefinementFilterViewModel(Filter.Facet(color, "green"))
        val viewGreen = CheckBoxFacetRefinement(toggleB)

        viewModelA.connectSearcher(searcher)
        viewModelA.connectView(viewRed)
        viewModelB.connectSearcher(searcher)
        viewModelB.connectView(viewGreen)

        // TODO Chip | size: 42
        // TODO Toggle | popular: true/false

        // RefinementList | color: OR | CountDesc,AlphaAsc | limit=5
        val viewModelList = RefinementFacetsViewModel(SelectionMode.Single)
        val viewList = RefinementFacetsAdapter()
        val presenterList = RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)

        viewModelList.connectSearcher(color, searcher, RefinementOperator.Or)
        viewModelList.connectView(viewList, presenterList)
        configureRecyclerView(list, viewList)

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
}



package com.algolia.instantsearch.sample.refinement.facet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import kotlinx.android.synthetic.main.refinement_list_activity.*
import refinement.RefinementOperator
import refinement.facet.list.*
import refinement.facet.list.FacetSortCriterion.*
import selection.SelectionMode

class RefinementFacetActivity : RefinementActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_list_activity)

        val colorAViewModel = RefinementFacetsViewModel(SelectionMode.Single)
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
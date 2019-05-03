package com.algolia.instantsearch.sample.refinement.filter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.instantsearch.sample.refinement.facet.RefinementFacetsAdapter
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import filter.toFilterGroups
import highlight
import kotlinx.android.synthetic.main.refinement_filter_demo.*
import refinement.RefinementOperator
import refinement.facet.RefinementFacetsViewModel
import refinement.facet.connectSearcher
import refinement.facet.connectView
import refinement.filter.RefinementFilterViewModel
import refinement.filter.connectSearcher
import refinement.filter.connectView
import search.SearcherSingleIndex
import selection.SelectableCompoundButton


class RefinementFilterDemo : AppCompatActivity() {

    private val popular = Attribute("color")
    private val promotions = Attribute("promotions")
    private val size = Attribute("size")

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
        )
    )
    private val index = client.initIndex(IndexName("mobile_demo_refinement_filter"))

    private val searcher = SearcherSingleIndex(index)

    private val colors
        get() = mapOf(
            popular.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            promotions.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            size.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refinement_filter_demo)

        val viewModelFreeShipping = RefinementFilterViewModel(Filter.Facet(promotions, "free shipping"))
        val viewFreeShipping = SelectableCompoundButton(checkBoxFreeShipping)

        viewModelFreeShipping.connectSearcher(searcher, RefinementOperator.Or)
        viewModelFreeShipping.connectView(viewFreeShipping)

        val viewModelCoupon = RefinementFilterViewModel(Filter.Facet(promotions, "coupon"))
        val viewCoupon = SelectableCompoundButton(switchCoupon)

        viewModelCoupon.connectSearcher(searcher, RefinementOperator.Or)
        viewModelCoupon.connectView(viewCoupon)
        
        val viewModelList = RefinementFacetsViewModel()
        val viewList = RefinementFacetsAdapter()

        viewModelList.connectSearcher(promotions, searcher, RefinementOperator.Or)
        viewModelList.connectView(viewList)
        configureRecyclerView(list, viewList)

        onChangeThenUpdateFiltersText(filtersTextView)
        onErrorThenUpdateFiltersText(filtersTextView)
        onClearAllThenClearFilters(filtersClearAll)
        updateFiltersTextFromState(filtersTextView)

        searcher.search()
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



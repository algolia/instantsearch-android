package com.algolia.instantsearch.sample.selectable.filter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.sample.R
import com.algolia.instantsearch.sample.selectable.facet.SelectableFacetsAdapter
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import filter.toFilterGroups
import highlight
import kotlinx.android.synthetic.main.selectable_filter_demo.*
import kotlinx.android.synthetic.main.selectable_header.*
import search.SearcherSingleIndex
import selectable.SelectableCompoundButton
import selectable.SelectableRadioGroup
import selectable.facet.SelectableFacetsViewModel
import selectable.facet.connectSearcher
import selectable.facet.connectView
import selectable.filter.SelectableFilterViewModel
import selectable.filter.connectSearcher
import selectable.filter.connectView
import selectable.filters.SelectableFiltersViewModel
import selectable.filters.connectSearcher
import selectable.filters.connectView


class SelectableFilterDemo : AppCompatActivity() {

    private val promotions = Attribute("promotions")
    private val size = Attribute("size")
    private val gender = Attribute("gender")
    private val tags = "_tags"

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db")
        )
    )
    private val index = client.initIndex(IndexName("mobile_demo_selectable_filter"))

    private val searcher = SearcherSingleIndex(index)

    private val colors
        get() = mapOf(
            promotions.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            size.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark),
            tags to ContextCompat.getColor(this, android.R.color.holo_purple),
            gender.raw to ContextCompat.getColor(this, android.R.color.holo_orange_light)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectable_filter_demo)

        val viewModelFreeShipping = SelectableFilterViewModel(Filter.Facet(promotions, "free shipping"))
        val viewFreeShipping = SelectableCompoundButton(checkBoxFreeShipping)

        viewModelFreeShipping.connectSearcher(searcher)
        viewModelFreeShipping.connectView(viewFreeShipping)

        val viewModelCoupon = SelectableFilterViewModel(Filter.Facet(promotions, "coupon"))
        val viewCoupon = SelectableCompoundButton(switchCoupon)

        viewModelCoupon.connectSearcher(searcher)
        viewModelCoupon.connectView(viewCoupon)

        val viewModelSize = SelectableFilterViewModel(Filter.Numeric(size, NumericOperator.Greater, 40))
        val viewSize = SelectableCompoundButton(checkBoxSize)

        viewModelSize.connectSearcher(searcher)
        viewModelSize.connectView(viewSize)

        val viewModelVintage = SelectableFilterViewModel(Filter.Tag("vintage"))
        val viewVintage = SelectableCompoundButton(checkBoxVintage)

        viewModelVintage.connectSearcher(searcher)
        viewModelVintage.connectView(viewVintage)

        val viewModelGender = SelectableFiltersViewModel(
            items = mapOf(
                R.id.male to Filter.Facet(gender, "male"),
                R.id.female to Filter.Facet(gender, "female")
            ),
            selected = R.id.male
        )
        val viewGender = SelectableRadioGroup(radioGroupGender)

        viewModelGender.connectSearcher(gender, searcher)
        viewModelGender.connectView(viewGender)

        val viewModelList = SelectableFacetsViewModel()
        val viewList = SelectableFacetsAdapter()

        viewModelList.connectSearcher(promotions, searcher)
        viewModelList.connectView(viewList)

        configureRecyclerView(list, viewList)
        onChangeThenUpdateFiltersText(filtersTextView)
        onErrorThenUpdateFiltersText(filtersTextView)
        onClearAllThenClearFilters(filtersClearAll)
        updateFiltersTextFromState(filtersTextView)
        onResponseChangedThenUpdateStats()

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

    private fun onResponseChangedThenUpdateStats() {
        searcher.onResponseChanged += {
            nbHits.text = getString(R.string.nb_hits, it.nbHits)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }

    private fun configureRecyclerView(
        recyclerView: View,
        view: SelectableFacetsAdapter
    ) {
        (recyclerView as RecyclerView).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = view
            it.itemAnimator = null
        }
    }
}



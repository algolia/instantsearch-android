package com.algolia.instantsearch.demo.selectable.filter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.selectable.facet.SelectableFacetsAdapter
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.selectable_filters_demo.*
import kotlinx.android.synthetic.main.selectable_header.*
import searcher.SearcherSingleIndex
import selectable.SelectableCompoundButton
import selectable.SelectableRadioGroup
import selectable.facet.SelectableFacetsViewModel
import selectable.facet.connectSearcher
import selectable.facet.connectView
import selectable.filter.SelectableFilterViewModel
import selectable.filter.SelectableFiltersViewModel
import selectable.filter.connectSearcher
import selectable.filter.connectView


class SelectableFiltersDemo : AppCompatActivity() {

    private val promotions = Attribute("promotions")
    private val size = Attribute("size")
    private val gender = Attribute("gender")
    private val tags = "_tags"
    private val colors
        get() = mapOf(
            promotions.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            size.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark),
            tags to ContextCompat.getColor(this, android.R.color.holo_purple),
            gender.raw to ContextCompat.getColor(this, android.R.color.holo_orange_light)
        )

    private val index = client.initIndex(IndexName("mobile_demo_selectable_filter"))
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectable_filters_demo)

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
        onChangeThenUpdateFiltersText(searcher, colors, filtersTextView)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onClearAllThenClearFilters(searcher, filtersClearAll)
        onResponseChangedThenUpdateNbHits(searcher)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}



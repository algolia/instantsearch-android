package com.algolia.instantsearch.demo.selectable.numeric

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.selectable_header.*
import kotlinx.android.synthetic.main.selectable_numerics_demo.*
import searcher.SearcherSingleIndex
import selectable.numeric.SelectableNumericsViewModel
import selectable.numeric.connectSearcher
import selectable.numeric.connectView


class SelectableNumericsDemo : AppCompatActivity() {

    private val price = Attribute("price")
    private val colors
        get() = mapOf(
            price.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )
    private val index = client.initIndex(IndexName("mobile_demo_selectable_numerics"))
    private val searcher = SearcherSingleIndex(index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectable_numerics_demo)

        val viewModel = SelectableNumericsViewModel(
            listOf(
                Filter.Numeric(price, NumericOperator.Less, 5),
                Filter.Numeric(price, 5..10),
                Filter.Numeric(price, 10..25),
                Filter.Numeric(price, 25..100),
                Filter.Numeric(price, NumericOperator.Greater, 100)
            )
        )
        val view = SelectableNumericsAdapter()

        viewModel.connectSearcher(price, searcher)
        viewModel.connectView(view)
        configureRecyclerView(listNumerics, view)

        onChangeThenUpdateFiltersText(searcher, colors, filtersTextView)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onClearAllThenClearFilters(searcher, filtersClearAll)
        onResponseChangedThenUpdateStats(searcher)
    }
}
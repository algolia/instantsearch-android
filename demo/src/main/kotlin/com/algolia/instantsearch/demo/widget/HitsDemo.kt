package com.algolia.instantsearch.demo.widget

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.android.searcher.connectSearchView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_hits.*

class HitsDemo : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val index = client.initIndex(IndexName("mobile_demo_facet_list_search"))
    private val colors
        get() = mapOf(brand.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark))
    private val searcher = SearcherSingleIndex(index)
    private val filterState = FilterState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_hits)
        setSupportActionBar(toolbar)

        val viewModel = HitsViewModel(searcher, filterState)
        val hitsAdapter = HitsAdapter()

        viewModel.pagedHits.observeNotNull(this, hitsAdapter::submitList)

        searcher.connectSearchView(searchView, filterState)
        searcher.onResponseChanged += {
            //            viewModel.updateData() TODO: How can I update data when searcher gets a new response?
        }
        configureRecyclerView(list, hitsAdapter)
        configureSearchView(searchView)

        searcher.errorListeners += { throwable ->
            throwable.printStackTrace()
        }
        onChangeThenUpdateFiltersText(filterState, colors, nbHits)
        onResponseChangedThenUpdateNbHits(searcher, false)
    }

    fun log(msg: String) {//FIXME remove after
        print(msg)
        Log.d("LOG", msg)
    }
}

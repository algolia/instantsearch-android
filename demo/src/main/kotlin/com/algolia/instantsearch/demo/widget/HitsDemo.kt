package com.algolia.instantsearch.demo.widget

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.android.searcher.connectSearchView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.widget.HitsViewModel
import com.algolia.instantsearch.helper.widget.connectSearcher
import com.algolia.instantsearch.helper.widget.connectView
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearch
import kotlinx.android.synthetic.main.demo_hits.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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

        val viewModel = HitsViewModel(listOf(ResponseSearch.Hit(JsonObject(mapOf("initial state!" to JsonPrimitive(42))))))
        val view = HitsAdapter()

        viewModel.connectView(view)
        viewModel.connectSearcher(searcher)
        searcher.connectSearchView(searchView, filterState)
        configureRecyclerView(list, view)
        configureSearchView(searchView)

        searcher.errorListeners += { throwable ->
            throwable.printStackTrace()
        }
        onChangeThenUpdateFiltersText(filterState, colors, nbHits)
        onResponseChangedThenUpdateNbHits(searcher, false)

        searcher.search(filterState)
    }

    fun log(msg: String) {
        print(msg)
        Log.d("LOG", msg)
    }
}

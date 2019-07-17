package com.algolia.instantsearch.demo.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.demo_home.*
import kotlinx.android.synthetic.main.include_search.*


class HomeDemo : AppCompatActivity() {

    private val index = client.initIndex(IndexName("mobile_demo_home"))
    private val searcher = SearcherSingleIndex(index, Query(hitsPerPage = 100))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_home)

        val adapter = HomeAdapter()

        configureRecyclerView(list, adapter)
        configureSearchView(searchView, getString(R.string.search_demos))
        configureSearchBox(searchView, searcher)

        searcher.response.subscribe { response ->
            if (response != null) {
                val hits = response.hits.deserialize(HomeHit.serializer())
                    .filter { homeActivities.containsKey(it.objectID) }
                    .groupBy { it.type }
                    .toSortedMap()
                    .flatMap { (key, value) ->
                        listOf(HomeItem.Header(key)) + value.map { HomeItem.Item(it) }.sortedBy { it.hit.objectID.raw }
                    }

                adapter.submitList(hits)
            }
        }
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}
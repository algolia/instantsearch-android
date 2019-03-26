package com.algolia.instantsearch.android.sample.views

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.sample.HitsAdapter
import com.algolia.search.model.response.ResponseSearch.Hit
import searcher.SearcherSingleIndex

class Hits(view: RecyclerView) {
    private val adapter = HitsAdapter(listOf())

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(view.context)
        val dividerItemDecoration =
            DividerItemDecoration(view.context, (view.layoutManager as LinearLayoutManager).orientation)
        view.addItemDecoration(dividerItemDecoration)
    }

    fun updateHits(newValue: List<Hit>) {
        adapter.hits = newValue
        adapter.notifyDataSetChanged()
    }

    fun connectWithSearcher(searcher: SearcherSingleIndex) {
        searcher.responseListeners += {
            updateHits(it.hits)
        }
    }
}
package com.algolia.instantsearch.demo.hits.paging

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.demo.hits.HitsAdapter
import com.algolia.instantsearch.helper.searcher.Searcher

fun Searcher.connectSearchView(searchView: SearchView, hitsAdapter: HitsAdapter) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            setQuery(newText)
            hitsAdapter.currentList?.dataSource?.invalidate()
            return true
        }
    })
}
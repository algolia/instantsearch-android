package com.algolia.instantsearch.android.searcher

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherForFacet
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


fun SearcherSingleIndex.connectSearchView(searchView: SearchView, filterState: FilterState) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            query.query = newText
            search(filterState)
            return true
        }
    })
}

fun SearcherForFacet.connectSearchView(searchView: SearchView, filterState: FilterState) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            facetQuery.facetQuery = newText
            search(filterState)
            return true
        }
    })
}
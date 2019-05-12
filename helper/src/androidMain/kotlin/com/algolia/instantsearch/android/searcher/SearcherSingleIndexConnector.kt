package com.algolia.instantsearch.android.searcher

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.helper.searcher.SearcherForFacet
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


fun SearcherSingleIndex.connectSearchView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            query.query = newText
            search()
            return true
        }
    })
}

fun SearcherForFacet.connectSearchView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            facetQuery.facetQuery = newText
            search()
            return true
        }
    })
}
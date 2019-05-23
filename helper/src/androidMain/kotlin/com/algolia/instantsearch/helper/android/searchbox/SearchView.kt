package com.algolia.instantsearch.helper.android.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


fun SearchBoxViewModel.connectSearchView(searchView: android.widget.SearchView) {
    searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            this@connectSearchView.query = query
            submitQuery()
            return false
        }

        override fun onQueryTextChange(query: String?): Boolean {
            this@connectSearchView.query = query
            return false
        }
    })
    searchView.setQuery(query, false)
}
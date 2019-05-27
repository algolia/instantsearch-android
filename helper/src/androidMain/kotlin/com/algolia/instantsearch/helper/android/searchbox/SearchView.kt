package com.algolia.instantsearch.helper.android.searchbox

import androidx.appcompat.widget.SearchView
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
    fun updateQuery(query: String? = this.query) = searchView.setQuery(query, false)
    updateQuery()
    onQueryChanged += {
        if (it != searchView.query) {
            updateQuery(it)
        }
    }
}

// TODO: searchbox.TestSearchBoxSearchView
fun SearchBoxViewModel.connectSearchView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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
    fun updateQuery(query: String? = this.query) = searchView.setQuery(query, false)
    updateQuery()
    onQueryChanged += {
        if (it != searchView.query) {
            updateQuery(it)
        }
    }
}
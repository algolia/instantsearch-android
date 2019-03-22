package com.algolia.instantsearch.android.sample.views

import androidx.appcompat.widget.SearchView

sealed class SearchBox {
    // TODO: AutoFocus
    var onQueryTextChangeListener: (String?) -> Unit = {}

    class System(private val searchView: android.widget.SearchView) : SearchBox() {
        init {
            searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    onQueryTextChangeListener(newText)
                    return true
                }
            })
        }
    }

    class Support(private val searchView: SearchView) : SearchBox() {
        init {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    onQueryTextChangeListener(newText)
                    return true
                }
            })
        }
    }
}
package com.algolia.instantsearch.android.sample.views

import androidx.appcompat.widget.SearchView

sealed class SearchBox(var onQueryTextChangeListener: QueryTextChangeListener) {
    // TODO: AutoFocus


    class System(searchView: android.widget.SearchView, listener: QueryTextChangeListener = null) :
        SearchBox(listener) {
        init {
            searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    onQueryTextChangeListener?.invoke(newText)
                    return true
                }
            })
        }
    }

    class Support(searchView: SearchView, listener: QueryTextChangeListener = null) :
        SearchBox(listener) {
        init {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    onQueryTextChangeListener?.invoke(newText)
                    return true
                }
            })
        }
    }
}
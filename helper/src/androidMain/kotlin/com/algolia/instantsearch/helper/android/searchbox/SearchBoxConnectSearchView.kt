package com.algolia.instantsearch.helper.android.searchbox

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


fun SearchBoxViewModel.connectView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            query = newText
            submit()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            query = newText
            return true
        }
    })
}
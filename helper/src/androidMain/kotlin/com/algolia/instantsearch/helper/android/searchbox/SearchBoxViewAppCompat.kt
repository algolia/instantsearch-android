package com.algolia.instantsearch.helper.android.searchbox

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.core.searchbox.SearchBoxView


public class SearchBoxViewAppCompat(val searchView: SearchView) : SearchBoxView {

    override var onQueryChanged: ((String?) -> Unit)? = null
    override var onQuerySubmitted: ((String?) -> Unit)? = null

    init {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                onQuerySubmitted?.invoke(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                onQueryChanged?.invoke(query)
                return false
            }
        })
    }

    override fun setQuery(query: String?) {
        searchView.setQuery(query, false)
    }
}
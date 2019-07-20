package com.algolia.instantsearch.helper.android.searchbox

import android.widget.SearchView
import com.algolia.instantsearch.core.event.Callback
import com.algolia.instantsearch.core.searchbox.SearchBoxView


public class SearchBoxViewImpl(
    public val searchView: SearchView
) : SearchBoxView {

    override var onQueryChanged: Callback<String?>? = null
    override var onQuerySubmitted: Callback<String?>? = null

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

    override fun setText(text: String?) {
        searchView.setQuery(text, false)
    }
}
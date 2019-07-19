package com.algolia.instantsearch.helper.android.searchbox

import androidx.appcompat.widget.SearchView
import com.algolia.instantsearch.core.event.Event
import com.algolia.instantsearch.core.searchbox.SearchBoxView


public class SearchBoxViewAppCompat(
    public val searchView: SearchView
) : SearchBoxView {

    override var onQueryChanged: Event<String?> = null
    override var onQuerySubmitted: Event<String?> = null

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
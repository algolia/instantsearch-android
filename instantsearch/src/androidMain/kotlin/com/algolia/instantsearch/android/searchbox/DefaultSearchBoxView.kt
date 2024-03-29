package com.algolia.instantsearch.android.searchbox

import android.widget.SearchView
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.stats.DefaultStatsPresenter

public class DefaultSearchBoxView(
    public val searchView: SearchView,
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

    override fun setText(text: String?, submitQuery: Boolean) {
        searchView.setQuery(text, submitQuery)
    }
}

@Deprecated(message = "use DefaultSearchBoxView instead", replaceWith = ReplaceWith("DefaultSearchBoxView"))
public typealias SearchBoxViewImpl = DefaultSearchBoxView

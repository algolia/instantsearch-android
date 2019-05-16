package com.algolia.instantsearch.android.paging

import androidx.appcompat.widget.SearchView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.searcher.Searcher

/**
 * Connects the Searcher to a SearchView and PagedListAdapter,
 * Triggering a search on new queries via the adapter's dataSource.
 */
fun <T, VH : RecyclerView.ViewHolder> Searcher.connectSearchView(
    searchView: SearchView,
    pagedListAdapter: PagedListAdapter<T, VH>
) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            setQuery(newText)
            pagedListAdapter.currentList?.dataSource?.invalidate()
            return true
        }
    })
}
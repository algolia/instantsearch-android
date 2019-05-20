package com.algolia.instantsearch.demo.searchbox

import android.os.Bundle
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.DemoActivity
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.configureRecyclerView
import com.algolia.instantsearch.demo.configureSearchView
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapter
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.connectSearchBoxViewModel
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_paging.*

class SearchOnSubmitDemo : DemoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search_on_submit)
        setSupportActionBar(toolbar)


        val searchBoxViewModel = SearchBoxViewModel()
        searchBoxViewModel.connectView(searchView)
        searcher.connectSearchBoxViewModel(searchBoxViewModel, false)

        val view = MovieAdapter()
        searcher.onResponseChanged += {
            view.submitList(it.hits.deserialize(Movie.serializer()))
        }
        configureRecyclerView(list, view)
        configureSearchView(searchView, false)

        searcher.search()
    }
}

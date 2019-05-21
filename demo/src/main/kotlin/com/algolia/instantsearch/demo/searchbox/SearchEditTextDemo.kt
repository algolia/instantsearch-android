package com.algolia.instantsearch.demo.searchbox

import android.os.Bundle
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.DemoActivity
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.configureRecyclerView
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapter
import com.algolia.instantsearch.helper.android.searchbox.SearchBox
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_search_edittext.*

class SearchEditTextDemo : DemoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search_edittext)
        setSupportActionBar(toolbar)


        val searchBoxViewModel = SearchBoxViewModel()
        searchBoxViewModel.connectView(SearchBox.EditText.System(editText))
        searchBoxViewModel.connectSearcher(searcher)

        val view = MovieAdapter()
        searcher.onResponseChanged += {
            view.submitList(it.hits.deserialize(Movie.serializer()))
        }
        configureRecyclerView(list, view)

        searcher.search()
    }
}

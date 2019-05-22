package com.algolia.instantsearch.demo.searchbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapter
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_paging.*


class SearchOnSubmitDemo : AppCompatActivity() {
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search_on_submit)
        setSupportActionBar(toolbar)

        searcher.index = client.initIndex(intent.indexName)

        val searchBoxViewModel = SearchBoxViewModel()
        searchBoxViewModel.connectView(searchView)
        searchBoxViewModel.connectSearcher(searcher, false)

        val view = MovieAdapter()
        searcher.onResponseChanged += {
            view.submitList(it.hits.deserialize(Movie.serializer()))
        }
        configureRecyclerView(list, view)
        configureSearchView(searchView, false)

        searcher.search()
    }
}

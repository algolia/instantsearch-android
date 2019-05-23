package com.algolia.instantsearch.demo.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.demo.list.MovieAdapter
import com.algolia.instantsearch.helper.android.searchbox.connectSearchView
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_search.*


class SearchOnSubmitDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search)
        setSupportActionBar(toolbar)

        searcher.index = client.initIndex(intent.indexName)

        val searchBoxViewModel = SearchBoxViewModel()
        val adapter = MovieAdapter()

        searchBoxViewModel.connectSearchView(searchView)
        searchBoxViewModel.connectSearcher(searcher, searchAsYouType = false)

        searcher.onResponseChanged += {
            adapter.submitList(it.hits.deserialize(Movie.serializer()))
        }

        configureRecyclerView(list, adapter)
        configureSearchView(searchView)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}

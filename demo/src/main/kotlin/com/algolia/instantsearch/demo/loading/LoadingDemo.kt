package com.algolia.instantsearch.demo.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapter
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.loading.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.demo_loading.*
import kotlinx.android.synthetic.main.demo_search.list
import kotlinx.android.synthetic.main.demo_search.toolbar
import kotlinx.android.synthetic.main.include_search.*


class LoadingDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_loading)

        val viewModel = LoadingViewModel()
        val view = LoadingViewSwipeRefreshLayout(swipeRefreshLayout)

        viewModel.connectView(view)
        viewModel.connectSearcher(searcher)

        val adapter = MovieAdapter()

        searcher.onResponseChanged += {
            adapter.submitList(it.hits.deserialize(Movie.serializer()))
        }

        configureSearcher(searcher)
        configureToolbar(toolbar)
        configureSearchBox(searchView, searcher)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}
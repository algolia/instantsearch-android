package com.algolia.instantsearch.demo.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.disconnect
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapterPaged
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectSearcher
import com.algolia.instantsearch.helper.loading.LoadingWidget
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import kotlinx.android.synthetic.main.demo_loading.*
import kotlinx.android.synthetic.main.demo_search.list
import kotlinx.android.synthetic.main.demo_search.toolbar
import kotlinx.android.synthetic.main.include_search.*


class LoadingDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Movie.serializer())
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val movies = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    private val widget = LoadingWidget(searcher)
    private lateinit var connections: List<Connection>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_loading)

        val view = LoadingViewSwipeRefreshLayout(swipeRefreshLayout)

        connections = widget.with(view)

        val adapter = MovieAdapterPaged()

        val searchBoxViewModel = SearchBoxViewModel()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        movies.observe(this, Observer { hits -> adapter.submitList(hits) })

        searchBoxViewModel.connectView(searchBoxView)
        searchBoxViewModel.connectSearcher(searcher, movies)

        configureSearcher(searcher)
        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        widget.disconnect()
        connections.disconnect()
    }
}
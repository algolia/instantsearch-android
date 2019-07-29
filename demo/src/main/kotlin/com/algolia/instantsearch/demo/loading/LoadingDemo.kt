package com.algolia.instantsearch.demo.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapterPaged
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxWidgetPagedList
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.loading.LoadingWidget
import com.algolia.instantsearch.helper.loading.connectView
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
    private val widgetLoading = LoadingWidget(searcher)
    private val widgetSearchBox = SearchBoxWidgetPagedList(searcher, listOf(movies))
    private val connection = ConnectionHandler(widgetLoading, widgetSearchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_loading)

        val view = LoadingViewSwipeRefreshLayout(swipeRefreshLayout)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val adapter = MovieAdapterPaged()

        connection.apply {
            +widgetLoading.connectView(view)
            +widgetSearchBox.connectView(searchBoxView)
        }
        movies.observe(this, Observer { hits -> adapter.submitList(hits) })

        configureSearcher(searcher)
        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}
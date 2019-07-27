package com.algolia.instantsearch.demo.list.paging

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
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxWidgetPagedList
import com.algolia.instantsearch.helper.android.searchbox.connectionView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import kotlinx.android.synthetic.main.demo_paging.*
import kotlinx.android.synthetic.main.include_search.*


class PagingSingleIndexDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Movie.serializer())
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val movies = LivePagedListBuilder<Int, Movie>(dataSourceFactory, pagedListConfig).build()
    private val widgetSearchBox = SearchBoxWidgetPagedList(searcher, listOf(movies))
    private val connection = ConnectionHandler(widgetSearchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_paging)

        val adapter = MovieAdapterPaged()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection.apply {
            +widgetSearchBox.connectionView(searchBoxView)
        }

        movies.observe(this, Observer { hits -> adapter.submitList(hits) })

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}
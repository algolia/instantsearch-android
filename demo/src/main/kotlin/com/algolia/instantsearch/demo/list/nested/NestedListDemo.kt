package com.algolia.instantsearch.demo.list.nested

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.actor.Actor
import com.algolia.instantsearch.demo.list.actor.ActorAdapterNested
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapterNested
import com.algolia.instantsearch.helper.android.list.SearcherMultipleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxWidgetPagedList
import com.algolia.instantsearch.helper.android.searchbox.connectionView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class NestedListDemo : AppCompatActivity() {

    private val searcher = SearcherMultipleIndex(
        client,
        listOf(
            IndexQuery(IndexName("mobile_demo_movies"), Query()),
            IndexQuery(IndexName("mobile_demo_actors"), Query())
        )
    )
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val moviesFactory = SearcherMultipleIndexDataSource.Factory(searcher, 0, Movie.serializer())
    private val actorsFactory = SearcherMultipleIndexDataSource.Factory(searcher, 1, Actor.serializer())
    private val movies = LivePagedListBuilder<Int, Movie>(moviesFactory, pagedListConfig).build()
    private val actors = LivePagedListBuilder<Int, Actor>(actorsFactory, pagedListConfig).build()
    private val widgetSearchBox = SearchBoxWidgetPagedList(searcher, listOf(movies, actors))
    private val connection = ConnectionHandler(widgetSearchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search)

        val adapterActor = ActorAdapterNested()
        val adapterMovie = MovieAdapterNested()
        val adapter = NestedListAdapter()

        actors.observe(this, Observer { hits -> adapterActor.submitList(hits) })
        movies.observe(this, Observer { hits -> adapterMovie.submitList(hits) })

        adapter.submitList(
            listOf(
                NestedListItem.Header("Movies"),
                NestedListItem.Movies(adapterMovie),
                NestedListItem.Header("Actors"),
                NestedListItem.Actors(adapterActor)
            )
        )

        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection.apply {
            +widgetSearchBox.connectionView(searchBoxView)
        }

        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}
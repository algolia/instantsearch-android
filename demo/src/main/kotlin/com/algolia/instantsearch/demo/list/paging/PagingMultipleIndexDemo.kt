package com.algolia.instantsearch.demo.list.paging

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
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class PagingMultipleIndexDemo : AppCompatActivity() {

    private val searcher = SearcherMultipleIndex(
        client,
        listOf(
            IndexQuery(IndexName("mobile_demo_movies")),
            IndexQuery(IndexName("mobile_demo_actors"))
        )
    )
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val moviesFactory = SearcherMultipleIndexDataSource.Factory(searcher, 0, Movie.serializer())
    private val actorsFactory = SearcherMultipleIndexDataSource.Factory(searcher, 1, Actor.serializer())
    private val movies = LivePagedListBuilder<Int, Movie>(moviesFactory, pagedListConfig).build()
    private val actors = LivePagedListBuilder<Int, Actor>(actorsFactory, pagedListConfig).build()
    private val searchBox = SearchBoxConnectorPagedList(searcher, listOf(movies, actors))
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_search)

        val adapterActor = ActorAdapterNested()
        val adapterMovie = MovieAdapterNested()
        val adapter = PagingMultipleIndexAdapter()

        actors.observe(this, Observer { hits -> adapterActor.submitList(hits) })
        movies.observe(this, Observer { hits -> adapterMovie.submitList(hits) })

        adapter.submitList(
            listOf(
                PagingMultipleIndexItem.Header("Movies"),
                PagingMultipleIndexItem.Movies(adapterMovie),
                PagingMultipleIndexItem.Header("Actors"),
                PagingMultipleIndexItem.Actors(adapterActor)
            )
        )

        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)

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
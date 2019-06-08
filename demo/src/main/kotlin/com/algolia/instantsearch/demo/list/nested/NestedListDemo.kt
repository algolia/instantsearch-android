package com.algolia.instantsearch.demo.list.nested

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.list.actor.Actor
import com.algolia.instantsearch.demo.list.actor.ActorAdapterNested
import com.algolia.instantsearch.demo.list.movie.Movie
import com.algolia.instantsearch.demo.list.movie.MovieAdapterNested
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.demo_search.*
import kotlinx.android.synthetic.main.include_search.*


class NestedListDemo : AppCompatActivity() {

    private val searcherMovies = SearcherSingleIndex(index = client.initIndex(IndexName("mobile_demo_movies")))
    private val searcherActors = SearcherSingleIndex(index = client.initIndex(IndexName("mobile_demo_actors")))
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    private val factoryMovies = SearcherSingleIndexDataSource.Factory(searcherMovies, Movie.serializer())
    private val factoryActors = SearcherSingleIndexDataSource.Factory(searcherActors, Actor.serializer())
    private val movies = LivePagedListBuilder<Int, Movie>(factoryMovies, pagedListConfig).build()
    private val actors = LivePagedListBuilder<Int, Actor>(factoryActors, pagedListConfig).build()

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

        val searchBoxViewModel = SearchBoxViewModel()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        searchBoxViewModel.connectView(searchBoxView)
        searchBoxViewModel.onItemChanged += {
            searcherMovies.setQuery(it)
            searcherActors.setQuery(it)
            movies.value?.dataSource?.invalidate()
            actors.value?.dataSource?.invalidate()
        }

        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(list, adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcherActors.cancel()
        searcherMovies.cancel()
    }
}
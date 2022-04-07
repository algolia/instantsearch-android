package com.algolia.instantsearch.examples.showcase.view.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingConfig
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.liveData
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.examples.R
import com.algolia.instantsearch.examples.databinding.IncludeSearchBinding
import com.algolia.instantsearch.examples.databinding.ShowcaseSearchBinding
import com.algolia.instantsearch.examples.showcase.shared.model.Actor
import com.algolia.instantsearch.examples.showcase.shared.model.Movie
import com.algolia.instantsearch.examples.showcase.view.client
import com.algolia.instantsearch.examples.showcase.view.configureRecyclerView
import com.algolia.instantsearch.examples.showcase.view.configureSearchView
import com.algolia.instantsearch.examples.showcase.view.configureToolbar
import com.algolia.instantsearch.examples.showcase.view.list.actor.ActorAdapterNested
import com.algolia.instantsearch.examples.showcase.view.list.movie.MovieAdapterNested
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.model.IndexName

class PagingMultipleIndexShowcase : AppCompatActivity() {

    private val multiSearcher = MultiSearcher(client)
    private val moviesSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_movies"))
    private val actorsSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_actors"))
    private val pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = false)
    private val moviesPaginator = Paginator(moviesSearcher, pagingConfig) { it.deserialize(Movie.serializer()) }
    private val actorsPaginator = Paginator(actorsSearcher, pagingConfig) { it.deserialize(Actor.serializer()) }
    private val searchBox = SearchBoxConnector(multiSearcher)
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseSearchBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val adapterActor = ActorAdapterNested()
        val adapterMovie = MovieAdapterNested()
        val adapter = PagingMultiSearchAdapter()

        actorsPaginator.liveData.observe(this) { adapterActor.submitData(lifecycle, it) }
        moviesPaginator.liveData.observe(this) { adapterMovie.submitData(lifecycle, it) }

        adapter.submitList(
            listOf(
                PagingMultipleIndexItem.Header("Movies"),
                PagingMultipleIndexItem.Movies(adapterMovie),
                PagingMultipleIndexItem.Header("Actors"),
                PagingMultipleIndexItem.Actors(adapterActor)
            )
        )

        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)
        connection += searchBox.connectView(searchBoxView)
        connection += searchBox.connectPaginator(moviesPaginator)
        connection += searchBox.connectPaginator(actorsPaginator)

        configureToolbar(binding.toolbar)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureRecyclerView(binding.hits, adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connection.clear()
    }
}

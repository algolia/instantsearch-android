package com.algolia.instantsearch.showcase.list.merged

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseMultisearchBinding
import com.algolia.instantsearch.showcase.list.actor.Actor
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapter
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class MergedListShowcase : AppCompatActivity() {

    private val multiSearcher = MultiSearcher(client)
    private val actorsSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("mobile_demo_actors"),
        query = Query(hitsPerPage = 5)
    )
    private val moviesSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("mobile_demo_movies"),
        query = Query(hitsPerPage = 3)
    )
    private val searchBox = SearchBoxConnector(multiSearcher)
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseMultisearchBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val actorsAdapter = ActorsAdapter()
        val moviesAdapter = MovieAdapter()
        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += actorsSearcher.connectHitsView(actorsAdapter) { response ->
            response.hits.deserialize(Actor.serializer())
        }
        connection += moviesSearcher.connectHitsView(moviesAdapter) { response ->
            response.hits.deserialize(Movie.serializer())
        }

        configureToolbar(binding.toolbar)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureTitle(binding.title1, getString(R.string.actors))
        configureTitle(binding.title2, getString(R.string.movies))
        configureRecyclerView(binding.hits1, actorsAdapter)
        configureRecyclerView(binding.hits2, moviesAdapter)

        multiSearcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connection.clear()
    }
}

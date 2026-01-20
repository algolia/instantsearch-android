package com.algolia.instantsearch.examples.android.codex.multisearch

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.examples.android.databinding.IncludeSearchBinding
import com.algolia.instantsearch.examples.android.databinding.ShowcaseMultisearchBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.showQueryHintIcon
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class MainActivity : ComponentActivity() {

    private val multiSearcher = MultiSearcher(
        appId = "latency",
        apiKey = "1f6fd3a6fb973cb08419fe7d288fa4db"
    )
    private val actorsSearcher = multiSearcher.addHitsSearcher("mobile_demo_actors", Query(hitsPerPage = 5))
    private val moviesSearcher = multiSearcher.addHitsSearcher("mobile_demo_movies")
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseMultisearchBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)
        connections += searchBoxConnector.connectView(searchBoxView)

        val actorAdapter = ActorAdapter()
        connections += actorsSearcher.connectHitsView(actorAdapter) { response -> response.hits.deserialize(Actor.serializer()) }
        binding.hits1.let {
            it.visibility = View.VISIBLE
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = actorAdapter
            it.itemAnimator = null
        }

        val movieAdapter = MovieAdapter()
        connections += moviesSearcher.connectHitsView(movieAdapter) { response -> response.hits.deserialize(Movie.serializer()) }
        binding.hits2.let {
            it.visibility = View.VISIBLE
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = movieAdapter
            it.itemAnimator = null
        }

        binding.title1.text = getString(R.string.actors)
        binding.title2.text = getString(R.string.movies)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))

        multiSearcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connections.clear()
    }

    fun ComponentActivity.configureSearchView(
        searchView: SearchView,
        queryHint: String
    ) {
        searchView.also {
            val hintIcon = ContextCompat.getDrawable(this, R.drawable.ic_search_hint)!!

            it.queryHint = queryHint
            it.setIconifiedByDefault(false)
            it.setOnQueryTextFocusChangeListener { _, hasFocus ->
                searchView.showQueryHintIcon(!hasFocus, hintIcon, queryHint)
            }
            searchView.showQueryHintIcon(true, hintIcon, queryHint)
        }
    }
}

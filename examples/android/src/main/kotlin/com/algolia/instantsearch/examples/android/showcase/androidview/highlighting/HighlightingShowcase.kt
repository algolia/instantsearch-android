package com.algolia.instantsearch.examples.android.showcase.androidview.highlighting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.examples.android.R
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.examples.android.showcase.androidview.*
import com.algolia.instantsearch.examples.android.databinding.IncludeSearchBinding
import com.algolia.instantsearch.examples.android.databinding.ShowcaseHighlightingBinding
import com.algolia.instantsearch.examples.android.showcase.androidview.list.movie.Movie
import com.algolia.search.helper.deserialize

class HighlightingShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseHighlightingBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val adapter = HighlightingAdapter()

        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Movie.serializer())
        }

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureRecyclerView(binding.hits, adapter)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureSearchBox(searchBinding.searchView, searcher, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}

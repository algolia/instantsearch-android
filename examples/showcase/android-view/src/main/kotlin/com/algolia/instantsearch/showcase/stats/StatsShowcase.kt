package com.algolia.instantsearch.showcase.stats

import android.os.Bundle
import android.text.SpannedString
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.stats.StatsTextView
import com.algolia.instantsearch.android.stats.StatsTextViewSpanned
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.StatsPresenter
import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.instantsearch.stats.connectView
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseStatsBinding

class StatsShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val stats = StatsConnector(searcher)
    private val connection = ConnectionHandler(stats)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseStatsBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val statsViewA = StatsTextView(binding.statsA)
        val statsViewB = StatsTextViewSpanned(binding.statsB)
        val presenter: StatsPresenter<SpannedString> = { response ->
            buildSpannedString {
                if (response != null) {
                    bold { append(response.nbHits.toString()) }
                    append(" ${getString(R.string.hits)}")
                    val query = searcher.query.query

                    if (query != null && query.isNotBlank()) {
                        append(" for \"$query\"")
                    }
                }
            }
        }

        connection += stats.connectView(statsViewA, StatsPresenterImpl())
        connection += stats.connectView(statsViewB, presenter)

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
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

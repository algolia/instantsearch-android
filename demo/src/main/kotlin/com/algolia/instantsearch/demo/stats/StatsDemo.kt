package com.algolia.instantsearch.demo.stats

import android.os.Bundle
import android.text.SpannedString
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.stats.StatsTextView
import com.algolia.instantsearch.helper.android.stats.StatsTextViewSpanned
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.*
import kotlinx.android.synthetic.main.demo_paging.toolbar
import kotlinx.android.synthetic.main.demo_stats.*
import kotlinx.android.synthetic.main.include_search.*


class StatsDemo : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_stats)

        val statsViewModel = StatsViewModel()
        val statsViewA = StatsTextView(statsA)
        val statsViewB = StatsTextViewSpanned(statsB)

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

        statsViewModel.connectSearcher(searcher)
        statsViewModel.connectView(statsViewA, true, StatsPresenterImpl())
        statsViewModel.connectView(statsViewB, true, presenter)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureSearchBox(searchView, searcher)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}
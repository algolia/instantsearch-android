package com.algolia.instantsearch.demo

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlight
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.demo_header.*


val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.NONE
    )
)

fun AppCompatActivity.onChangeThenUpdateFiltersText(
    searcher: SearcherSingleIndex,
    colors: Map<String, Int>,
    filtersTextView: TextView
) {
    filtersTextView.text = searcher.filterState.toFilterGroups().highlight(colors = colors)
    searcher.filterState.onChange += {
        filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
    }
}

fun AppCompatActivity.onClearAllThenClearFilters(searcher: SearcherSingleIndex, filtersClearAll: View) {
    filtersClearAll.setOnClickListener {
        searcher.filterState.notify { clear() }
    }
}

fun AppCompatActivity.onErrorThenUpdateFiltersText(searcher: SearcherSingleIndex, filtersTextView: TextView) {
    searcher.errorListeners += {
        filtersTextView.text = it.localizedMessage
    }
}

fun AppCompatActivity.onResponseChangedThenUpdateStats(searcher: SearcherSingleIndex) {
    searcher.onResponseChanged += {
        nbHits.text = getString(R.string.nb_hits, it.nbHits)
    }
}

fun AppCompatActivity.onResponseChangedThenUpdateNbHits(searcher: SearcherSingleIndex) {
    searcher.onResponseChanged += {
        nbHits.text = getString(R.string.nb_hits, it.nbHits)
    }
}

fun AppCompatActivity.configureRecyclerView(
    recyclerView: View,
    view: RecyclerView.Adapter<*>
) {
    (recyclerView as RecyclerView).let {
        it.layoutManager = LinearLayoutManager(this)
        it.adapter = view
        it.itemAnimator = null
    }
}
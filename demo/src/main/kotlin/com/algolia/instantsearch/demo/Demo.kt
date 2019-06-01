package com.algolia.instantsearch.demo

import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.ImageSpan
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.filter.clear.FilterClearViewImpl
import com.algolia.instantsearch.helper.android.highlight
import com.algolia.instantsearch.helper.android.stats.StatsTextViewSpanned
import com.algolia.instantsearch.helper.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsPresenter
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectSearcher
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName
import io.ktor.client.features.logging.LogLevel


val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.NONE
    )
)

val stubIndex = client.initIndex(IndexName("stub"))

fun AppCompatActivity.configureToolbar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.let {
        it.title = intent.extras?.getString(KeyName)
        it.setDisplayHomeAsUpEnabled(true)

    }
}

fun AppCompatActivity.onFilterChangedThenUpdateFiltersText(
    filterState: FilterState,
    colors: Map<String, Int>,
    filtersTextView: TextView
) {
    filtersTextView.text = filterState.toFilterGroups().highlight(colors = colors)
    filterState.onChanged += {
        filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
    }
}

fun AppCompatActivity.onClearAllThenClearFilters(
    filterState: FilterState,
    filtersClearAll: View
) {
    val viewModel = FilterClearViewModel()

    viewModel.connectView(FilterClearViewImpl(filtersClearAll))
    viewModel.connectFilterState(filterState)
}

fun AppCompatActivity.onErrorThenUpdateFiltersText(
    searcher: SearcherSingleIndex,
    filtersTextView: TextView
) {
    searcher.onErrorChanged += {
        filtersTextView.text = it.localizedMessage
    }
}

fun AppCompatActivity.onResponseChangedThenUpdateNbHits(
    searcher: SearcherSingleIndex,
    nbHitsView: TextView
) {
    val viewModel = StatsViewModel()
    val view = StatsTextViewSpanned(nbHitsView)
    val presenter: StatsPresenter<SpannedString> = { response ->
        buildSpannedString {
            if (response != null) {
                bold { append(response.nbHits.toString()) }
                append(" ${getString(R.string.hits)}")
            }
        }
    }

    viewModel.connectSearcher(searcher)
    viewModel.connectView(view, presenter)
}

fun AppCompatActivity.configureTitle(
    textView: TextView,
    text: String,
    color: Int
) {
    textView.let {
        it.text = text
        it.setTextColor(color)
        it.visibility = View.VISIBLE
    }
}

fun AppCompatActivity.configureSearcher(searcher: SearcherSingleIndex) {
    searcher.index = client.initIndex(intent.indexName)
}

fun AppCompatActivity.configureSearcher(searcher: SearcherForFacets) {
    searcher.index = client.initIndex(intent.indexName)
}

fun AppCompatActivity.configureRecyclerView(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>
) {
    recyclerView.let {
        it.visibility = View.VISIBLE
        it.layoutManager = LinearLayoutManager(this)
        it.adapter = adapter
        it.itemAnimator = null
    }
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart == 0) {
                recyclerView.scrollToPosition(0)
            }
        }
    })
}

val Intent.indexName: IndexName get() = IndexName(extras!!.getString(KeyIndexName)!!)

fun AppCompatActivity.configureSearchView(
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

fun SearchView.showQueryHintIcon(
    showIconHint: Boolean,
    hintIcon: Drawable,
    hintText: String? = null
) {
    queryHint = if (!showIconHint) {
        hintText
    } else {
        val textView = findViewById<AutoCompleteTextView>(R.id.search_src_text)
        val textSize = (textView.textSize * 1.25).toInt()

        hintIcon.setBounds(0, 0, textSize, textSize)
        SpannableStringBuilder("    ").also {
            it.setSpan(ImageSpan(hintIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (hintText != null) it.append(hintText) else it.append(" ")
        }
    }
}
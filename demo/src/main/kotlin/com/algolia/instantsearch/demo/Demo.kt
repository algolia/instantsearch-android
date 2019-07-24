package com.algolia.instantsearch.demo

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.android.filter.FilterClearViewImpl
import com.algolia.instantsearch.helper.android.highlight
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.stats.StatsTextViewSpanned
import com.algolia.instantsearch.helper.filter.clear.FilterClearWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsPresenter
import com.algolia.instantsearch.helper.stats.StatsViewModel
import com.algolia.instantsearch.helper.stats.connectSearcher
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.Compression
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName
import io.ktor.client.features.logging.LogLevel


val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        readTimeout = 5000,
        logLevel = LogLevel.ALL,
        compression = Compression.None
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
    filtersTextView: TextView,
    vararg attributes: Attribute
) {
    val colors = attributes.mapIndexed { index, attribute ->
        attribute.raw to when (index) {
            0 -> ContextCompat.getColor(this, android.R.color.holo_red_dark)
            1 -> ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            2 -> ContextCompat.getColor(this, android.R.color.holo_green_dark)
            3 -> ContextCompat.getColor(this, android.R.color.holo_purple)
            else -> Color.BLACK
        }
    }.toMap()
    filtersTextView.text = filterState.toFilterGroups().highlight(colors = colors)
    filterState.filters.subscribe {
        filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
    }
}

fun AppCompatActivity.onClearAllThenClearFilters(
    filterState: FilterState,
    filtersClearAll: View
) {
    FilterClearWidget(filterState).with(FilterClearViewImpl(filtersClearAll))
}

fun AppCompatActivity.onErrorThenUpdateFiltersText(
    searcher: SearcherSingleIndex,
    filtersTextView: TextView
) {
    searcher.error.subscribe {
        filtersTextView.text = it?.localizedMessage
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
    viewModel.connectView(view, true, presenter)
}

fun AppCompatActivity.onResetThenRestoreFilters(
    reset: View,
    filterState: FilterState,
    filters: Map<FilterGroupID, Set<Filter>>
) {
    reset.setOnClickListener {
        filterState.notify { set(filters) }
    }
}

fun AppCompatActivity.configureTitle(
    textView: TextView,
    text: String
) {
    textView.let {
        it.text = text
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
        it.autoScrollToStart(adapter)
    }
}

fun RecyclerView.autoScrollToStart(adapter: RecyclerView.Adapter<*>) {
    adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart == 0) {
                scrollToPosition(0)
            }
        }
    })
}

val Intent.indexName: IndexName get() = IndexName(extras!!.getString(KeyIndexName)!!)

fun <R> AppCompatActivity.configureSearchBox(
    searchView: SearchView,
    searcher: Searcher<R>
) {
    val searchBoxViewModel = SearchBoxViewModel()
    val searchBoxView = SearchBoxViewAppCompat(searchView)

    searchBoxViewModel.connectView(searchBoxView)
    searchBoxViewModel.connectSearcher(searcher)
}

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

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
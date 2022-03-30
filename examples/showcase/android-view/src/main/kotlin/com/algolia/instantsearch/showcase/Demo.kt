package com.algolia.instantsearch.showcase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.algolia.instantsearch.android.filter.clear.FilterClearViewImpl
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.android.stats.StatsTextViewSpanned
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.filter.clear.FilterClearConnector
import com.algolia.instantsearch.filter.clear.connectView
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.toFilterGroups
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.StatsPresenter
import com.algolia.instantsearch.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName
import io.ktor.client.features.logging.*


val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.ALL
    )
)

val stubIndexName = IndexName("stub")
val stubIndex = client.initIndex(stubIndexName)

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
    filtersClearAll: View,
    connection: ConnectionHandler
) {
    val connector = FilterClearConnector(filterState)

    connection += connector
    connection += connector.connectView(FilterClearViewImpl(filtersClearAll))
}

fun AppCompatActivity.onErrorThenUpdateFiltersText(
    searcher: Searcher<*>,
    filtersTextView: TextView
) {
    searcher.error.subscribe {
        filtersTextView.text = it?.localizedMessage
    }
}

fun AppCompatActivity.onResponseChangedThenUpdateNbHits(
    searcher: Searcher<ResponseSearch>,
    nbHitsView: TextView,
    connection: ConnectionHandler
) {
    val view = StatsTextViewSpanned(nbHitsView)
    val presenter: StatsPresenter<SpannedString> = { response ->
        buildSpannedString {
            if (response != null) {
                bold { append(response.nbHits.toString()) }
                append(" ${getString(R.string.hits)}")
            }
        }
    }
    val connector = StatsConnector(searcher)

    connection += connector
    connection += connector.connectView(view, presenter)
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

fun AppCompatActivity.configureSearcher(searcher: IndexNameHolder) {
    searcher.indexName = intent.indexName
}

fun Activity.configureSearcher(searcher: IndexNameHolder) {
    searcher.indexName = intent.indexName
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

fun RecyclerView.configure(
    recyclerViewAdapter: RecyclerView.Adapter<*>
) {
    visibility = View.VISIBLE
    layoutManager = LinearLayoutManager(context)
    adapter = recyclerViewAdapter
    itemAnimator = null
    autoScrollToStart(recyclerViewAdapter)
}

val Intent.indexName: IndexName get() = IndexName(extras!!.getString(KeyIndexName)!!)

fun <R> AppCompatActivity.configureSearchBox(
    searchView: SearchView,
    searcher: Searcher<R>,
    connection: ConnectionHandler
) {
    val connector = SearchBoxConnector(searcher)

    connection += connector
    connection += connector.connectView(SearchBoxViewAppCompat(searchView))
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

val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(context)

public fun Set<FilterGroup<*>>.highlight(
    converter: FilterGroupsConverter<Set<FilterGroup<*>>, String?> = FilterGroupsConverter.SQL.Unquoted,
    colors: Map<String, Int> = mapOf(),
    defaultColor: Int = Color.BLACK
): SpannableStringBuilder {
    return SpannableStringBuilder().also {
        var begin = 0

        forEachIndexed { index, group ->
            val color = colors.getOrElse(group.name ?: "") { defaultColor }
            val string = converter(setOf(group))

            it.append(string)
            it.setSpan(
                ForegroundColorSpan(color),
                begin,
                it.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (index < size - 1) {
                begin = it.length
                it.append(" AND ")
                it.setSpan(
                    StyleSpan(Typeface.BOLD),
                    begin,
                    it.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            begin = it.length
        }
    }
}

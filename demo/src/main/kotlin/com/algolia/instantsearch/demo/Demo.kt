package com.algolia.instantsearch.demo

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlight
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.header_filter.nbHits as filterNbHits


val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.ALL
    )
)

fun AppCompatActivity.onChangeThenUpdateFiltersText(
    filterState: FilterState,
    colors: Map<String, Int>,
    filtersTextView: TextView
) {
    filtersTextView.text = filterState.toFilterGroups().highlight(colors = colors)
    filterState.onChange += {
        filtersTextView.text = it.toFilterGroups().highlight(colors = colors)
    }
}

fun AppCompatActivity.onClearAllThenClearFilters(filterState: FilterState, filtersClearAll: View) {
    filtersClearAll.setOnClickListener {
        filterState.notify { clear() }
    }
}

fun AppCompatActivity.onErrorThenUpdateFiltersText(searcher: SearcherSingleIndex, filtersTextView: TextView) {
    searcher.onErrorChanged += {
        filtersTextView.text = it.localizedMessage
    }
}

fun AppCompatActivity.onResponseChangedThenUpdateNbHits(
    searcher: SearcherSingleIndex,
    nbHitsView: TextView = filterNbHits
) {
    searcher.onResponseChanged += {
        nbHitsView.text = getString(R.string.nb_hits, it.nbHits)
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

fun AppCompatActivity.configureSearchView(
    searchView: SearchView
) {
    searchView.also {
        it.isSubmitButtonEnabled = false
        it.isFocusable = true
        it.setIconifiedByDefault(false)
        it.setOnQueryTextFocusChangeListener { _, hasFocus ->
            val hintIcon = ContextCompat.getDrawable(this, R.drawable.ic_search_hint)!!
            val hintText = getString(R.string.search_for_brands)

            searchView.showQueryHintIcon(!hasFocus, hintIcon, hintText)
        }
    }
}

fun SearchView.showQueryHintIcon(showIconHint: Boolean, hintIcon: Drawable, hintText: String? = null) {
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
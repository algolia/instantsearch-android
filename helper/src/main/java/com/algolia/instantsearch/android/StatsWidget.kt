package com.algolia.instantsearch.android;

import android.widget.TextView
import com.algolia.search.client.Index
import searcher.Searcher
import searcher.SearcherSingleQuery

class StatsWidget(private val view: TextView) {
    public fun foo() {
        val index: Index("asd")
        val searcher: Searcher = SearcherSingleQuery()
    }
}

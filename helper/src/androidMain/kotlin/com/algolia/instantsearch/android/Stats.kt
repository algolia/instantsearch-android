package com.algolia.instantsearch.android;

import android.widget.TextView
import com.algolia.search.model.response.ResponseSearch
import searcher.SearcherSingleIndex

class Stats(private val view: TextView) {
    fun updateView(res: ResponseSearch){
        view.text = "%d hits in %d ms".format(res.nbHits, res.processingTimeMS)
    }

    fun connectWithSearcher(searcher: SearcherSingleIndex) {
        searcher.responseListeners += {
            updateView(it)
        }
    }
}

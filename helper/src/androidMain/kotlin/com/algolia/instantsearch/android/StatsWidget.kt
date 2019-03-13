package com.algolia.instantsearch.android;

import android.widget.TextView
import com.algolia.search.model.response.ResponseSearch

class StatsWidget(private val view: TextView) {
    fun updateView(res: ResponseSearch){
        view.post { view.text = "%d hits in %d ms".format(res.nbHits, res.processingTimeMS) }
    }
}

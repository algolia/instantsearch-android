package com.algolia.instantsearch.helper.android.stats

import android.widget.TextView
import com.algolia.instantsearch.helper.stats.StatsView


public class StatsTextView(
    public val view: TextView
): StatsView {

    override fun setItem(item: String) {
        view.text = item
    }
}
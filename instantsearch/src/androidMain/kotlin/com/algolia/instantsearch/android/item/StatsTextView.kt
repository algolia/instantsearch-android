package com.algolia.instantsearch.android.item

import android.widget.TextView
import com.algolia.instantsearch.stats.StatsView

public class StatsTextView(
    public val view: TextView,
) : StatsView<String> {

    override fun setText(text: String) {
        view.text = text
    }
}

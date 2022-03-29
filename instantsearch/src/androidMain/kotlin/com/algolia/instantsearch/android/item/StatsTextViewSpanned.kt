package com.algolia.instantsearch.android.item

import android.text.SpannedString
import android.widget.TextView
import com.algolia.instantsearch.stats.StatsView

public class StatsTextViewSpanned(
    public val view: TextView,
) : StatsView<SpannedString> {

    override fun setText(text: SpannedString) {
        view.text = text
    }
}

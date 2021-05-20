package com.algolia.instantsearch.helper.android.item

import android.text.SpannedString
import android.widget.TextView
import com.algolia.instantsearch.helper.stats.StatsView

public class StatsTextViewSpanned(
    public val view: TextView,
) : StatsView<SpannedString> {

    override fun setText(text: SpannedString) {
        view.text = text
    }
}

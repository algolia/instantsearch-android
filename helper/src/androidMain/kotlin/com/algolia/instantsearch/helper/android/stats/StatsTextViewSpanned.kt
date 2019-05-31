package com.algolia.instantsearch.helper.android.stats

import android.text.SpannedString
import android.widget.TextView
import com.algolia.instantsearch.core.item.ItemView


public class StatsTextViewSpanned(val view: TextView) : ItemView<SpannedString> {

    override fun setItem(item: SpannedString) {
        view.text = item
    }
}
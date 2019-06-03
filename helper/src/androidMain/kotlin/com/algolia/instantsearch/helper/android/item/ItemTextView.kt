package com.algolia.instantsearch.helper.android.item

import android.widget.TextView
import com.algolia.instantsearch.core.item.ItemView


public class ItemTextView(
    public val view: TextView
) : ItemView<String> {

    override fun setItem(item: String) {
        view.text = item
    }
}
package com.algolia.instantsearch.helper.android.filter

import android.view.View
import com.algolia.instantsearch.helper.filter.clear.FilterClearView


public class FilterClearViewImpl(
    public val view: View
) : FilterClearView {

    override var onClick: ((Unit) -> Unit)? = null

    init {
        view.setOnClickListener { onClick?.invoke(Unit) }
    }
}
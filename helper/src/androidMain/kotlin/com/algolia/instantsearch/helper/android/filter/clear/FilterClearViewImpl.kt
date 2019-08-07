package com.algolia.instantsearch.helper.android.filter.clear

import android.view.View
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.filter.clear.FilterClearView


public class FilterClearViewImpl(
    public val view: View
) : FilterClearView {

    override var onClear: Callback<Unit>? = null

    init {
        view.setOnClickListener { onClear?.invoke(Unit) }
    }
}
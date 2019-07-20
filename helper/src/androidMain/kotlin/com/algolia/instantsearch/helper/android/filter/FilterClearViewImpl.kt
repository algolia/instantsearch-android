package com.algolia.instantsearch.helper.android.filter

import android.view.View
import com.algolia.instantsearch.core.event.Event
import com.algolia.instantsearch.helper.filter.clear.FilterClearView


public class FilterClearViewImpl(
    public val view: View
) : FilterClearView {

    override var onClear: Event<Unit> = null

    init {
        view.setOnClickListener { onClear?.invoke(Unit) }
    }
}
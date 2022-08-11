package com.algolia.instantsearch.compose.filter.clear.internal

import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.core.Callback

/**
 * Implementation of [FilterClear].
 */
internal class FilterClearImpl : FilterClear {

    override var onClear: Callback<Unit>? = null

    init {
        trace()
    }

    override fun clear() {
        onClear?.invoke(Unit)
    }
}

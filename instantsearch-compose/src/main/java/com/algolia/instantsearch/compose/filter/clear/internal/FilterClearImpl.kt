package com.algolia.instantsearch.compose.filter.clear.internal

import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.core.Callback

/**
 * Implementation of [FilterClear].
 */
internal class FilterClearImpl : FilterClear {

    override var onClear: Callback<Unit>? = null

    override fun clear() {
        onClear?.invoke(Unit)
    }
}

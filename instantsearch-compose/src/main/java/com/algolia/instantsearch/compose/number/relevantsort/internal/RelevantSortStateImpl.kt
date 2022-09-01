package com.algolia.instantsearch.compose.number.relevantsort.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.number.relevantsort.RelevantSortState

/**
 * Implementation of [RelevantSortState].
 *
 * @param value initial value
 */
internal class RelevantSortStateImpl<T>(value: T) : RelevantSortState<T> {

    override var sort: T by mutableStateOf(value)
    override var didToggle: (() -> Unit)? = null

    init {
        trace()
    }

    override fun updateView(input: T) {
        this.sort = input
    }
}

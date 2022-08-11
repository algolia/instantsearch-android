package com.algolia.instantsearch.compose.sortby.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.sortby.SortByState
import com.algolia.instantsearch.core.Callback

/**
 * Implementation of [SortByState].
 *
 * @param options sort by option values
 * @param selectedOption index of selected option
 */
internal class SortByStateImpl(
    options: Map<Int, String>,
    selectedOption: Int?
) : SortByState {

    @set:JvmName("_select")
    override var selected: Int? by mutableStateOf(selectedOption)
    override var options: Map<Int, String> by mutableStateOf(options)
    override var onSelectionChange: Callback<Int>? = null

    init {
        trace()
    }

    override fun setMap(map: Map<Int, String>) {
        this.options = map
    }

    override fun setSelected(selected: Int?) {
        this.selected = selected
    }
}

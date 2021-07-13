package com.algolia.instantsearch.compose.sortby.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    override var options: Map<Int, String> by mutableStateOf(options)
    override var selectedOption: Int? by mutableStateOf(selectedOption)
    override var onSelectionChange: Callback<Int>? = null

    override fun setMap(map: Map<Int, String>) {
        this.options = map
    }

    override fun setSelected(selected: Int?) {
        this.selectedOption = selected
    }

    override fun selectOption(index: Int?) {
        this.selectedOption = index
    }
}

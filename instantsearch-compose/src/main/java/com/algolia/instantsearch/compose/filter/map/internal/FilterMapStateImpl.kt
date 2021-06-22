package com.algolia.instantsearch.compose.filter.map.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.map.FilterMapState
import com.algolia.instantsearch.core.Callback

/**
 * Implementation of [FilterMapState].
 *
 * @param radioOptions radio options list
 * @param selectedOption selected option index
 */
internal class FilterMapStateImpl(
    radioOptions: Map<Int, String>,
    selectedOption: Int?
) : FilterMapState {

    override var radioOptions: Map<Int, String> by mutableStateOf(radioOptions)
    override var selectedOption: Int? by mutableStateOf(selectedOption)
    override var onSelectionChange: Callback<Int>? = null

    override fun optionSelected(selected: Int) {
        onSelectionChange?.invoke(selected)
    }

    override fun setMap(map: Map<Int, String>) {
        this.radioOptions = map
    }

    override fun setSelected(selected: Int?) {
        this.selectedOption = selected
    }
}

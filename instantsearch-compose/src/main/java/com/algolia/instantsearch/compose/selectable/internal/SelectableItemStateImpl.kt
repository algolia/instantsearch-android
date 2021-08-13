package com.algolia.instantsearch.compose.selectable.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.selectable.SelectableItemState
import com.algolia.instantsearch.core.Callback

/**
 * Implementation of [SelectableItemState].
 *
 * @param item initial item value
 * @param isSelected initial isSelection value
 */
internal class SelectableItemStateImpl<T>(
    item: T,
    isSelected: Boolean
) : SelectableItemState<T> {

    @set:JvmName("_item")
    override var item: T by mutableStateOf(item)
    override var isSelected: Boolean by mutableStateOf(isSelected)
    override var onSelectionChanged: Callback<Boolean>? = null

    override fun setItem(item: T) {
        this.item = item
    }

    override fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}

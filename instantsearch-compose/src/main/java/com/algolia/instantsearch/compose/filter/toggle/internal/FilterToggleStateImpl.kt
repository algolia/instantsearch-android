package com.algolia.instantsearch.compose.filter.toggle.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.toggle.FilterToggleState
import com.algolia.instantsearch.core.Callback

internal class FilterToggleStateImpl(
    text: String,
    isSelected: Boolean
) : FilterToggleState {

    override var text: String by mutableStateOf(text)
    override var isSelected: Boolean by mutableStateOf(isSelected)
    override var onSelectionChanged: Callback<Boolean>? = null

    override fun setItem(item: String) {
        this.text = item
    }

    override fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    override fun selectionChanged(isSelected: Boolean) {
        onSelectionChanged?.invoke(isSelected)
    }
}

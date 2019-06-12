package com.algolia.instantsearch.helper.android.filter.current

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.algolia.instantsearch.helper.filter.current.CurrentFiltersView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


public class CurrentFiltersViewImpl(
    val view: ChipGroup,
    @LayoutRes val chipLayout: Int? = null
) : CurrentFiltersView {

    var filters = sortedMapOf<String, String>()

    override var onClick: ((String) -> Unit)? = null

    override fun setItem(item: Map<String, String>) {
        filters = item.toSortedMap()
        view.removeAllViews()
        filters.entries.forEach { (identifier, filter) ->
            val chip: Chip = if (chipLayout != null) {
                LayoutInflater.from(view.context).inflate(chipLayout, null) as Chip
            } else Chip(view.context)
            val onClickListener = View.OnClickListener {
                onClick?.invoke(identifier)
            }

            chip.text = filter
            chip.isCloseIconVisible = true
            chip.setOnClickListener(onClickListener)
            chip.setOnCloseIconClickListener(onClickListener)
            view.addView(chip)
        }
    }
}
package com.algolia.instantsearch.helper.android.filter.current

import android.view.View
import com.algolia.instantsearch.helper.filter.current.CurrentFiltersView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


public class CurrentFiltersViewImpl(
    val view: ChipGroup
) : CurrentFiltersView {

    var filters = sortedMapOf<String, String>()

    override var onClick: ((String) -> Unit)? = null

    override fun setItem(item: Map<String, String>) {
        filters = item.toSortedMap()
        updateView()
    }

    private fun updateView() {
        view.removeAllViews()
        filters.entries.forEach { (identifier, filter) ->
            val chip: Chip = Chip(view.context)
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
package com.algolia.instantsearch.helper.android.filter

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.algolia.instantsearch.helper.filter.current.FilterAndID
import com.algolia.instantsearch.helper.filter.current.FilterCurrentView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


public class FilterCurrentViewImpl(
    val view: ChipGroup,
    @LayoutRes val chipLayout: Int? = null
) : FilterCurrentView {

    override var onClick: ((FilterAndID) -> Unit)? = null

    override fun setItem(item: List<Pair<FilterAndID, String>>) {
        view.removeAllViews()
        item.forEach { (id, filter) ->
            val chip: Chip = if (chipLayout != null)
                LayoutInflater.from(view.context).inflate(chipLayout, null) as Chip else Chip(view.context)
            val onClickListener = View.OnClickListener {
                onClick?.invoke(id)
            }

            chip.text = filter
            chip.isCloseIconVisible = true
            chip.setOnClickListener(onClickListener)
            chip.setOnCloseIconClickListener(onClickListener)
            view.addView(chip)
        }
    }
}
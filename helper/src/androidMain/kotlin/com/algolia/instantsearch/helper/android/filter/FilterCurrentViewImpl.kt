package com.algolia.instantsearch.helper.android.filter

import android.view.View
import androidx.annotation.LayoutRes
import com.algolia.instantsearch.helper.android.inflate
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
            val chip: Chip = if (chipLayout != null) view.inflate<Chip>(chipLayout) else Chip(view.context)
            val onClickListener = View.OnClickListener { onClick?.invoke(id) }

            chip.let {
                it.text = filter
                it.isCloseIconVisible = true
                it.setOnClickListener(onClickListener)
                it.setOnCloseIconClickListener(onClickListener)
                view.addView(it)
            }
        }
    }
}
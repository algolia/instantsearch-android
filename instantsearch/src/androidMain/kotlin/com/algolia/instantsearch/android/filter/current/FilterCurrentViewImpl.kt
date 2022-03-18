package com.algolia.instantsearch.android.filter.current

import android.view.View
import androidx.annotation.LayoutRes
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.filter.current.FilterAndID
import com.algolia.instantsearch.filter.current.FilterCurrentView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

public class FilterCurrentViewImpl(
    public val view: ChipGroup,
    @LayoutRes public val chipLayout: Int? = null,
) : FilterCurrentView {

    override var onFilterSelected: Callback<FilterAndID>? = null

    override fun setFilters(filters: List<Pair<FilterAndID, String>>) {
        view.removeAllViews()
        filters.forEach { (id, filter) ->
            val chip: Chip = if (chipLayout != null) view.inflate<Chip>(chipLayout) else Chip(view.context)
            val onClickListener = View.OnClickListener { onFilterSelected?.invoke(id) }

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

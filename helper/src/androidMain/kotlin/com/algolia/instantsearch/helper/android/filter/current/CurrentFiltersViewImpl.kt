package com.algolia.instantsearch.helper.android.filter.current

import android.view.View
import com.algolia.instantsearch.helper.filter.current.CurrentFiltersView
import com.algolia.search.model.filter.Filter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


public class CurrentFiltersViewImpl(
    val view: ChipGroup
) : CurrentFiltersView {

    var filters = sortedMapOf<String, Filter>()

    override var onClick: ((String) -> Unit)? = null

    override fun setItems(items: Map<String, Filter>) {
        filters = items.toSortedMap()
        updateView()
    }

    private fun updateView() {
        view.removeAllViews()
        filters.entries.forEach {
            val identifier = it.key
            val filter = it.value
            val chip = Chip(view.context)

            chip.text = filter.toString()
            chip.setOnClickListener(View.OnClickListener {
                onClick?.invoke(identifier)
            })
            view.addView(chip)
        }
    }
}
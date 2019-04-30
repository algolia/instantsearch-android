package com.algolia.instantsearch.sample.refinementToggle

import android.widget.CheckBox
import com.algolia.search.model.filter.Filter
import refinement.facet.RefinementFilter
import refinement.facet.RefinementFilterView

class CheckBoxFacetRefinement(private val view: CheckBox) : RefinementFilterView {

    init {
        view.setOnClickListener { onClick?.invoke() }
    }

    override var onClick: (() -> Unit)? = null

    override fun setSelectableItem(selectableItem: RefinementFilter) {
        val (filter, isSelected) = selectableItem
        view.text = when (filter) {
            is Filter.Facet -> "${filter.attribute} : ${filter.value.raw}"
            is Filter.Tag -> filter.value
            is Filter.Numeric -> when (val value = filter.value) {
                is Filter.Numeric.Value.Comparison -> "${filter.attribute} ${value.operator} ${value.number}"
                is Filter.Numeric.Value.Range -> "${filter.attribute} ${value.lowerBound} to ${value.upperBound}"
            }
            else -> TODO("Should never happen, considering Filter is a sealed class ¯\\_(ツ)_/¯")
        }
        view.isChecked = isSelected
    }
}
package com.algolia.instantsearch.sample.refinementToggle

import android.widget.CheckBox
import com.algolia.search.model.search.Facet
import refinement.facet.RefinementFacet
import refinement.facet.RefinementFacetView

class CheckBoxFacetRefinement(val view: CheckBox, var item: Facet? = null) : RefinementFacetView {
    init {
        view.setOnClickListener { onClick?.invoke(item) }
    }

    override var onClick: ((Facet?) -> Unit)? = null

    override fun setSelectableItem(selectableItem: RefinementFacet?) {
        item = selectableItem?.first
        updateView(item, selectableItem?.second ?: view.isChecked)
    }

    private fun updateView(facet: Facet? = item, isSelected: Boolean = view.isChecked) {
        view.text = facet?.let { "${facet.value} (${facet.count})" } ?: "No Facet"
        view.isChecked = isSelected
    }
}
package com.algolia.instantsearch.helper.android.filter

import android.widget.CompoundButton
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleView


public class FilterToggleViewCompoundButton(
    public val compoundButton: CompoundButton
) : FilterToggleView,
    CompoundButton.OnCheckedChangeListener {

    override var onClick: ((Boolean) -> Unit)? = null

    init {
        compoundButton.setOnCheckedChangeListener(this)
    }

    override fun setItem(item: String) {
        compoundButton.text = item
    }

    override fun setIsSelected(isSelected: Boolean) {
        compoundButton.setOnCheckedChangeListener(null)
        compoundButton.isChecked = isSelected
        compoundButton.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        onClick?.invoke(isChecked)
    }
}
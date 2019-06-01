package com.algolia.instantsearch.helper.android.selectable

import android.widget.CompoundButton
import com.algolia.instantsearch.core.selectable.SelectableItemView


public class SelectableItemViewCompoundButton(
    public val compoundButton: CompoundButton
) : SelectableItemView, CompoundButton.OnCheckedChangeListener {

    override var onClick: ((Boolean) -> Unit)? = null

    init {
        compoundButton.setOnCheckedChangeListener(this)
    }

    override fun setText(text: String) {
        compoundButton.text = text
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
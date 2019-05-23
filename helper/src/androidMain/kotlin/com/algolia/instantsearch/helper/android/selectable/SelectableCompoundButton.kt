package com.algolia.instantsearch.helper.android.selectable

import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import com.algolia.instantsearch.core.selectable.SelectableItemView


public class SelectableCompoundButton(
    val view: CompoundButton
) : SelectableItemView, CompoundButton.OnCheckedChangeListener {

    override var onClick: ((Boolean) -> Unit)? = null

    init {
        view.setOnCheckedChangeListener(this)
    }

    override fun setText(text: String) {
        view.text = text
    }

    override fun setIsSelected(isSelected: Boolean) {
        view.setOnCheckedChangeListener(null)
        view.isChecked = isSelected
        view.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        onClick?.invoke(isChecked)
    }
}
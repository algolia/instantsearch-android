package com.algolia.instantsearch.helper.android.selectable

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView


public class SelectableRadioGroup(
    val view: RadioGroup
) : SelectableSegmentView<Int, String>, RadioGroup.OnCheckedChangeListener {

    override var onClick: ((Int) -> Unit)? = null

    init {
        view.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        onClick?.invoke(checkedId)
    }

    override fun setSelected(selected: Int?) {
        view.setOnCheckedChangeListener(null)
        view.check(selected ?: View.NO_ID)
        view.setOnCheckedChangeListener(this)
    }

    override fun setItems(items: Map<Int, String>) {
        for (index in 0 until view.childCount) {
            val view = view.getChildAt(index) as? RadioButton

            items[view?.id]?.let { view?.text = it }
        }
    }
}
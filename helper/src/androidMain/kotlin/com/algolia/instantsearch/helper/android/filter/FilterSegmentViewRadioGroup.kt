package com.algolia.instantsearch.helper.android.filter

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.algolia.instantsearch.core.event.Event
import com.algolia.instantsearch.helper.filter.segment.FilterSegmentView


public class FilterSegmentViewRadioGroup(
    public val radioGroup: RadioGroup
) : FilterSegmentView,
    RadioGroup.OnCheckedChangeListener {

    override var onSelectionChange: Event<Int> = null

    init {
        radioGroup.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        onSelectionChange?.invoke(checkedId)
    }

    override fun setSelected(selected: Int?) {
        radioGroup.setOnCheckedChangeListener(null)
        radioGroup.check(selected ?: View.NO_ID)
        radioGroup.setOnCheckedChangeListener(this)
    }

    override fun setSegment(segment: Map<Int, String>) {
        for (index in 0 until radioGroup.childCount) {
            val view = radioGroup.getChildAt(index) as? RadioButton

            segment[view?.id]?.let { view?.text = it }
        }
    }
}
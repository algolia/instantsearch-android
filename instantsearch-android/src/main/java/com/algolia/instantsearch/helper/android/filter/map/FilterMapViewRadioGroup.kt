package com.algolia.instantsearch.helper.android.filter.map

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.filter.map.FilterMapView

public class FilterMapViewRadioGroup(
    public val radioGroup: RadioGroup,
) : FilterMapView,
    RadioGroup.OnCheckedChangeListener {

    override var onSelectionChange: Callback<Int>? = null

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

    override fun setMap(map: Map<Int, String>) {
        for (index in 0 until radioGroup.childCount) {
            val view = radioGroup.getChildAt(index) as? RadioButton

            map[view?.id]?.let { view?.text = it }
        }
    }
}

package com.algolia.instantsearch.helper.android.selectable

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView


public class SelectableSegmentViewSpinner(
    public val spinner: Spinner,
    public val adapter: ArrayAdapter<String>,
    public val defaultSelection: Int = 0
) : SelectableSegmentView<Int, String>, AdapterView.OnItemSelectedListener {

    override var onClick: ((Int) -> Unit)? = null

    init {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(defaultSelection)
    }

    override fun setSelected(selected: Int?) {
        spinner.setSelection(selected ?: defaultSelection)
    }

    override fun setItem(item: Map<Int, String>) {
        adapter.clear()
        adapter.addAll(item.values)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onClick?.invoke(position)
    }
}
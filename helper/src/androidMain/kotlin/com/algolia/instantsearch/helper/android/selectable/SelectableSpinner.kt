package com.algolia.instantsearch.helper.android.selectable

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentView


public class SelectableSpinner(
    val spinner: Spinner,
    val adapter: ArrayAdapter<String>,
    val defaultSelection: Int = 0
) : SelectableSegmentView<Int, String>, AdapterView.OnItemSelectedListener {

    override var onClick: ((Int) -> Unit)? = null

    init {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun setSelected(selected: Int?) {
        spinner.setSelection(selected ?: defaultSelection)
    }

    override fun setItems(items: Map<Int, String>) {
        adapter.clear()
        adapter.addAll(items.values)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onClick?.invoke(position)
    }
}
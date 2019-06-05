package com.algolia.instantsearch.helper.android.index

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.algolia.instantsearch.helper.index.IndexSegmentView


public class IndexSegmentViewSpinner(
    public val spinner: Spinner,
    public val adapter: ArrayAdapter<String>,
    public val defaultSelection: Int = 0
) : IndexSegmentView,
    AdapterView.OnItemSelectedListener {

    override var onClick: ((Int) -> Unit)? = null

    init {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        spinner.setSelection(defaultSelection)
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
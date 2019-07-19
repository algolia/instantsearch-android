package com.algolia.instantsearch.helper.android.index

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.algolia.instantsearch.core.event.Event
import com.algolia.instantsearch.helper.index.IndexSegmentView


public class IndexSegmentViewAutocomplete(
    val view: AutoCompleteTextView,
    val adapter: ArrayAdapter<String>
) : IndexSegmentView,
    AdapterView.OnItemClickListener {

    override var onSelectionChange: Event<Int> = null

    private var map: Map<Int, String>? = null

    init {
        view.setAdapter(adapter)
        view.onItemClickListener = this
    }

    override fun setSelected(selected: Int?) {
        map?.get(selected)?.let { view.setText(it, false) }
    }

    override fun setSegment(segment: Map<Int, String>) {
        map = segment
        adapter.setNotifyOnChange(false)
        adapter.clear()
        adapter.addAll(segment.values)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onSelectionChange?.invoke(position)
    }
}
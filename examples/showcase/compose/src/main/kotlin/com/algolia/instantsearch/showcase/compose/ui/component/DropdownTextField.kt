package com.algolia.instantsearch.showcase.compose.ui.component

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.algolia.instantsearch.compose.sortby.SortByState
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.R.id
import com.algolia.instantsearch.showcase.compose.R.layout
import com.google.android.material.textfield.TextInputLayout

@Composable
fun DropdownTextField(
    modifier: Modifier = Modifier,
    sortByState: SortByState,
    items: List<String> = emptyList()
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            (View.inflate(context, layout.autocompletetextfield, null) as TextInputLayout).apply {
                findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).apply {
                    inputType = EditorInfo.TYPE_NULL
                    onItemClickListener = onItemClickOf(sortByState)
                    val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
                    adapter.addAll(items)
                    setAdapter(adapter)
                    setSelected(sortByState)
                }
            }
        },
        update = {
            val autoComplete = it.findViewById<AutoCompleteTextView>(id.autoCompleteTextView)
            val adapter = autoComplete?.adapter?.asArrayAdapter()
            adapter?.setOptions(sortByState)
        }
    )
}

@Suppress("UNCHECKED_CAST")
private fun ListAdapter.asArrayAdapter() = this as? ArrayAdapter<String>

fun onItemClickOf(sortByState: SortByState) = AdapterView.OnItemClickListener { _, _, position, _ ->
    sortByState.onSelectionChange?.invoke(position)
}

private fun AutoCompleteTextView.setSelected(sortByState: SortByState) {
    val selected = sortByState.selected
    sortByState.options[selected]?.let { setText(it, false) }
}

private fun ArrayAdapter<String>.setOptions(sortByState: SortByState) {
    setNotifyOnChange(false)
    clear()
    addAll(sortByState.options.values)
    notifyDataSetChanged()
}

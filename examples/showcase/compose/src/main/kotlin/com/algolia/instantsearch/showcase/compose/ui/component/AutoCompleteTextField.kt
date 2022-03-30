package com.algolia.instantsearch.showcase.compose.ui.component

import android.text.Editable
import android.text.TextWatcher
import android.view.View.inflate
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.showcase.compose.R
import com.google.android.material.textfield.TextInputLayout

@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    label: String,
    suggestions: List<String> = emptyList()
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            (inflate(context, R.layout.autocompletetextfield, null) as TextInputLayout).apply {
                hint = label
                findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).apply {
                    addTextChangedListener(textWatcherOf(searchBoxState))
                    val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
                    setAdapter(adapter)
                }
            }
        },
        update = {
            val autoComplete = it.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
            val adapter = autoComplete.adapter.asArrayAdapter()
            adapter?.clear()
            adapter?.addAll(suggestions)
        }
    )
}

@Suppress("UNCHECKED_CAST")
private fun ListAdapter.asArrayAdapter() = this as? ArrayAdapter<String>

private fun textWatcherOf(searchBoxState: SearchBoxState) =
    object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchBoxState.onQueryChanged?.invoke(s?.toString())
        }
    }

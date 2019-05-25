package com.algolia.instantsearch.helper.android.searchbox

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.algolia.instantsearch.core.searchbox.SearchBoxView


public class SearchBoxViewEditText(val editText: EditText) : SearchBoxView {

    override var onQueryChanged: ((String?) -> Unit)? = null
    override var onQuerySubmitted: ((String?) -> Unit)? = null

    init {
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onQueryChanged?.invoke(s?.toString())
            }
        })
    }

    override fun setQuery(query: String?) {
        editText.setText(query)
    }
}
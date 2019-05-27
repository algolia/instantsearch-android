package com.algolia.instantsearch.helper.android.searchbox

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel


fun SearchBoxViewModel.connectEditText(editText: EditText) {
    editText.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            this@connectEditText.query = s.toString()
        }
    })
    fun updateQuery(query: String? = this.query) = editText.setText(query)
    updateQuery()
    onQueryChanged += {
        if (it != editText.text.toString()) {
            updateQuery(it)
        }
    }
}
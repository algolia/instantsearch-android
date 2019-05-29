package com.algolia.instantsearch.helper.android.filter.clear

import android.view.View
import com.algolia.instantsearch.helper.filter.clear.ClearFilterView

class ClearFilterViewImpl(private val view: View) : ClearFilterView {
    override var onClick: (() -> Unit)? = null
        set(value) {
            field = value
            view.setOnClickListener {
                field?.invoke()
            }
        }
}
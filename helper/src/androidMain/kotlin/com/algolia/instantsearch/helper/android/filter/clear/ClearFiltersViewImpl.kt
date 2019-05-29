package com.algolia.instantsearch.helper.android.filter.clear

import android.view.View
import com.algolia.instantsearch.helper.filter.clear.ClearFiltersView

public class ClearFiltersViewImpl(internal val view: View) : ClearFiltersView {
    override var onClick: (() -> Unit)? = null

    init {
        view.setOnClickListener { onClick?.invoke() }
    }
}
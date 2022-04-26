package com.algolia.instantsearch.android.filter.clear

import android.view.View
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.filter.clear.FilterClearView

public class DefaultFilterClearView(
    public val view: View,
) : FilterClearView {

    override var onClear: Callback<Unit>? = null

    init {
        view.setOnClickListener { onClear?.invoke(Unit) }
    }
}

@Deprecated(message = "use DefaultFilterClearView instead", replaceWith = ReplaceWith("DefaultFilterClearView"))
public typealias FilterClearViewImpl = DefaultFilterClearView

package com.algolia.instantsearch.filter.clear

import com.algolia.instantsearch.core.Callback

public interface FilterClearView {

    public var onClear: Callback<Unit>?
}

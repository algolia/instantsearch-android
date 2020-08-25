package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.Callback


public interface FilterClearView {

    public var onClear: Callback<Unit>?
}
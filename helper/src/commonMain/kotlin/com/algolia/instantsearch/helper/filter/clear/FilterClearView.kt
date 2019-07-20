package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.event.Callback


public interface FilterClearView {

    public var onClear: Callback<Unit>?
}
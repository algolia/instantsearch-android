package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.event.Event


public interface FilterClearView {

    public var onClear: Event<Unit>
}
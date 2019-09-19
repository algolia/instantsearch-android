package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.Callback

/**
 * A View that can trigger a clearing of filters.
 */
public interface FilterClearView {

    /**
     * A callback that you must call when the filters get cleared.
     */
    public var onClear: Callback<Unit>?
}
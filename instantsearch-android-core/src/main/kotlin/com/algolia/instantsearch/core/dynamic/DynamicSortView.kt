package com.algolia.instantsearch.core.dynamic

/**
 * View presenting the dynamic sort priority applied to the search and a toggle control.
 */
public interface DynamicSortView {

    /**
     * Lambda triggered by the controller when toggle interaction occurred
     * (for example, toggle button clicked or switch changed).
     */
    public var didToggle: (() -> Unit)?

    public fun setPriority(priority: DynamicSortPriority?)
}

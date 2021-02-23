package com.algolia.instantsearch.core.smartsort

/**
 * View presenting the smart sort priority applied to the search and a toggle control.
 */
public interface SmartSortView {

    /**
     * Lambda triggered by the controller when toggle interaction occurred
     * (for example, toggle button clicked or switch changed).
     */
    public var didToggle: (() -> Unit)?

    /**
     * Set a priority to the view.
     */
    public fun setPriority(priority: SmartSortPriority?)
}

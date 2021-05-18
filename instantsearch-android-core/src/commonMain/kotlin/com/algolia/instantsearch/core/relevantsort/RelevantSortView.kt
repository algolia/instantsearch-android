package com.algolia.instantsearch.core.relevantsort

/**
 * View presenting the relevant sort priority applied to the search and a toggle control.
 */
public interface RelevantSortView<in T> {

    /**
     * Lambda triggered by the controller when toggle interaction occurred
     * (for example, toggle button clicked or switch changed).
     */
    public var didToggle: (() -> Unit)?

    /**
     * Set a priority to the view.
     */
    public fun updateView(input: T)
}

/**
 * Convenience implementation of [RelevantSortView] with [RelevantSortPriority] as view update input.
 */
public typealias RelevantSortPriorityView = RelevantSortView<RelevantSortPriority?>

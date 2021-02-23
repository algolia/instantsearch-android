package com.algolia.instantsearch.core.smartsort

import com.algolia.instantsearch.core.smartsort.SmartSortPriority.HitsCount
import com.algolia.instantsearch.core.smartsort.SmartSortPriority.Relevancy
import com.algolia.instantsearch.core.subscription.SubscriptionValue

/**
 * The component that stores the currently applied smart sort priority applied to the search in
 * the dynamically sorted index (virtual replica) and provides the interface to toggle this value.
 *
 * Usage of the dynamically sorted index introduces the trade-off between the number of results and
 * the relevancy of results. DynamicSortToggle components provide a convenient interface to switch
 * between these options.
 */
public class SmartSortViewModel(
    priority: SmartSortPriority? = null,
) {

    /**
     * The priority to apply to the search in the dynamically sorted index.
     *
     * `null` value represents the undefined state, meaning that either the view model has never
     * been connected to a searcher, or the searched index is not the virtual replica.
     */
    public val priority: SubscriptionValue<SmartSortPriority?> = SubscriptionValue(priority)

    /**
     * Switch the smart sort priority to the opposite one.
     * Skipped if the current value of sort priority is `null`.
     */
    public fun toggle() {
        val value = priority.value ?: return
        priority.value = when (value) {
            Relevancy -> HitsCount
            HitsCount -> Relevancy
        }
    }
}

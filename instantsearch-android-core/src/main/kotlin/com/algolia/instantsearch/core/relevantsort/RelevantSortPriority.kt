package com.algolia.instantsearch.core.relevantsort

/**
 * Represents the priority to apply to the search in the dynamically sorted index
 *
 * @param relevancyStrictness relevancy strictness value to apply to the search
 */
public enum class RelevantSortPriority(
    public val relevancyStrictness: Int
) {

    /** Prioritize less more relevant results */
    Relevancy(100),

    /** Prioritize more less relevant results */
    HitsCount(0);

    public companion object {

        @JvmStatic
        public fun of(relevancyStrictness: Int): RelevantSortPriority = when (relevancyStrictness) {
            0 -> HitsCount
            else -> Relevancy
        }
    }
}

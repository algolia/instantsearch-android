package com.algolia.instantsearch.core.number.range


public data class Range<T>(
    public val min: T,
    public val max: T
) where T : Number, T : Comparable<T> {

    public companion object {

        public operator fun <T> invoke(range: ClosedRange<T>): Range<T> where T : Number, T : Comparable<T> {
            return Range(range.start, range.endInclusive)
        }
    }
}

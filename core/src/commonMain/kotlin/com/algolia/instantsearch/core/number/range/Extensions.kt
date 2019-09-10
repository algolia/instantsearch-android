package com.algolia.instantsearch.core.number.range

/**
 * Coerces a Range into another, ensuring its min and max fit within the other Range's.
 *
 * @param bounds the Range to coerce this Range into.
 * @return this if it was within the given [bounds], else a new Range coerced within.
 */
internal fun <T> Range<T>.coerce(bounds: Range<T>?): Range<T> where T : Number, T : Comparable<T> {
    return bounds?.let {
        val coercedMin = min.coerceIn(it.min, it.max)
        val coercedMax = max.coerceIn(it.min, it.max)

        Range(coercedMin, coercedMax)
    } ?: this
}

internal fun <T> T.coerce(bounds: Range<T>?): T where T : Number, T : Comparable<T> {
    return bounds?.let { coerceIn(it.min, it.max) } ?: this
}
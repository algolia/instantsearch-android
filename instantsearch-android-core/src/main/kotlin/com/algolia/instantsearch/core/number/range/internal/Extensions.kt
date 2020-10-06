package com.algolia.instantsearch.core.number.range.internal

import com.algolia.instantsearch.core.number.range.Range

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

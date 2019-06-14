package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.number.Range


internal fun Range<Int>.coerceInt(bounds: Range<Int>?): Range<Int> {
    return bounds?.let {
        val coercedMin = min.coerceIn(it.min, it.max)
        val coercedMax = max.coerceIn(it.min, it.max)

        Range.Int(coercedMin, coercedMax)
    } ?: this
}

internal fun Range<Long>.coerceLong(bounds: Range<Long>?): Range<Long> {
    return bounds?.let {
        val coercedMin = min.coerceIn(it.min, it.max)
        val coercedMax = max.coerceIn(it.min, it.max)

        Range.Long(coercedMin, coercedMax)
    } ?: this
}

internal fun Range<Float>.coerceFloat(bounds: Range<Float>?): Range<Float> {
    return bounds?.let {
        val coercedMin = min.coerceIn(it.min, it.max)
        val coercedMax = max.coerceIn(it.min, it.max)

        Range.Float(coercedMin, coercedMax)
    } ?: this
}

internal fun Range<Double>.coerceDouble(bounds: Range<Double>?): Range<Double> {
    return bounds?.let {
        val coercedMin = min.coerceIn(it.min, it.max)
        val coercedMax = max.coerceIn(it.min, it.max)

        Range.Double(coercedMin, coercedMax)
    } ?: this
}

internal fun Int.coerceInt(bounds: Range<Int>?): Int {
    return bounds?.let { coerceIn(it.min, it.max) } ?: this
}

internal fun Long.coerceLong(bounds: Range<Long>?): Long {
    return bounds?.let { coerceIn(it.min, it.max) } ?: this
}

internal fun Float.coerceFloat(bounds: Range<Float>?): Float {
    return bounds?.let { coerceIn(it.min, it.max) } ?: this
}

internal fun Double.coerceDouble(bounds: Range<Double>?): Double {
    return bounds?.let { coerceIn(it.min, it.max) } ?: this
}
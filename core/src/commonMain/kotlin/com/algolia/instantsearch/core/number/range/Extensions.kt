package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.number.Range
import kotlin.jvm.JvmName


@JvmName("coerceInInt")
public fun Range<Int>.coerceIn(range: Range<Int>?): Range<Int> {
    return range?.let {
        val coercedMin = if (min < range.min) range.min else min
        val coercedMax = if (max > range.max) range.max else max
        Range.Int(coercedMin, coercedMax)
    } ?: this
}

@JvmName("coerceInFloat")
public fun Range<Float>.coerceIn(range: Range<Float>?): Range<Float> {
    return range?.let {
        val coercedMin = if (min < range.min) range.min else min
        val coercedMax = if (max > range.max) range.max else max
        Range.Float(coercedMin, coercedMax)
    } ?: this
}

@JvmName("coerceInDouble")
public fun Range<Double>.coerceIn(range: Range<Double>?): Range<Double> {
    return range?.let {
        val coercedMin = if (min < range.min) range.min else min
        val coercedMax = if (max > range.max) range.max else max
        Range.Double(coercedMin, coercedMax)
    } ?: this
}

@JvmName("coerceInLong")
public fun Range<Long>.coerceIn(range: Range<Long>?): Range<Long> {
    return range?.let {
        val coercedMin = if (min < range.min) range.min else min
        val coercedMax = if (max > range.max) range.max else max
        Range.Long(coercedMin, coercedMax)
    } ?: this
}
package com.algolia.instantsearch.helper.filter.range.internal

import kotlin.reflect.KClass

/**
 * Get the corresponding mapper of a numeric type.
 *
 * @param clazz the numeric type class to get its mapper.
 */
@Suppress("UNCHECKED_CAST")
internal fun <T> mapperOf(clazz: KClass<T>): (Number) -> T where T : Number, T : Comparable<T> {
    return when (clazz) {
        Int::class -> { number -> number.toInt() as T }
        Double::class -> { number -> number.toDouble() as T }
        Long::class -> { number -> number.toLong() as T }
        Float::class -> { number -> number.toFloat() as T }
        Short::class -> { number -> number.toShort() as T }
        Byte::class -> { number -> number.toByte() as T }
        else -> throw UnsupportedOperationException("Couldn't inference the class type")
    }
}

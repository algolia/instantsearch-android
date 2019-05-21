package com.algolia.instantsearch.core.selectable.number


public typealias Computation<T> = (((T?) -> T?) -> Unit)

public fun Computation<Int>.increment(step: Int = 1, default: Int = 0) {
    this { it?.plus(step) ?: default }
}

public fun Computation<Long>.increment(step: Long = 1L, default: Long = 0L) {
    this{ it?.plus(step) ?: default }
}

public fun Computation<Float>.increment(step: Float = 1f, default: Float = 0f) {
    this { it?.plus(step) ?: default }
}

public fun Computation<Double>.increment(step: Double = 1.0, default: Double = 0.0) {
    this { it?.plus(step) ?: default }
}

public fun Computation<Int>.decrement(step: Int = 1, default: Int = 0) {
    this { it?.minus(step) ?: default }
}

public fun Computation<Long>.decrement(step: Long = 1L, default: Long = 0L) {
    this{ it?.minus(step) ?: default }
}

public fun Computation<Float>.decrement(step: Float = 1f, default: Float = 0f) {
    this { it?.minus(step) ?: default }
}

public fun Computation<Double>.decrement(step: Double = 1.0, default: Double = 0.0) {
    this { it?.minus(step) ?: default }
}

public fun <T: Number> Computation<T>.just(value: T?) {
    this { value }
}



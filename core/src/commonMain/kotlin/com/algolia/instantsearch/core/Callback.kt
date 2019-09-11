package com.algolia.instantsearch.core

import com.algolia.instantsearch.core.number.range.Range


/**
 * A Callback that can be invoked.
 */
public interface Callback<in T> {

    public operator fun invoke(item: T)
}


/**
 * The default [Callback] implementation.
 */
public class CallbackImpl<in T>(val block: (T) -> Unit) : Callback<T> {

    override fun invoke(item: T) = block.invoke(item)
}

/**
 * A [Callback] implementation without parameters.
 */
public class CallbackUnit(val block: (Unit) -> Unit) : Callback<Unit> {

    override fun invoke(item: Unit) = block.invoke(item)
}

/**
 * A [Callback] implementation for range.
 */
public class CallbackRange<T>(val block: (Range<T>?) -> Unit) :
    Callback<Range<T>?> where T : Number, T : Comparable<T> {

    override fun invoke(item: Range<T>?) {
        block.invoke(item)
    }
}
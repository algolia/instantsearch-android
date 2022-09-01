package com.algolia.instantsearch.compose.number.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.number.NumberState
import com.algolia.instantsearch.core.number.Computation

/**
 * Implementation of [NumberState].
 *
 * @param text initial text value
 * @param computation initial computation value
 */
internal class NumberStateImpl<T>(
    text: String,
    computation: Computation<T>
) : NumberState<T> where T : Number {

    @set:JvmName("_text")
    override var text: String by mutableStateOf(text)

    @set:JvmName("_computation")
    override var computation: Computation<T> by mutableStateOf(computation)

    init {
        trace()
    }

    override fun setText(text: String) {
        this.text = text
    }

    override fun setComputation(computation: Computation<T>) {
        this.computation = computation
    }
}

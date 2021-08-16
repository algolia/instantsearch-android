package com.algolia.instantsearch.compose.hits.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.hits.HitsState

/**
 * Implementation of [HitsState].
 *
 * @param hitsList initial hits list value
 */
internal class HitsStateImpl<T>(hitsList: List<T>) : HitsState<T> {

    @set:JvmName("_hits")
    override var hits: List<T> by mutableStateOf(hitsList)

    override fun setHits(hits: List<T>) {
        this.hits = hits
    }
}

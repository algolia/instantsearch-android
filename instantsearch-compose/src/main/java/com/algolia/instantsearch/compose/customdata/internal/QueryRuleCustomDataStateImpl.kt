package com.algolia.instantsearch.compose.customdata.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.customdata.QueryRuleCustomDataState
import com.algolia.instantsearch.compose.internal.trace

internal class QueryRuleCustomDataStateImpl<T>(initialItem: T?) : QueryRuleCustomDataState<T> {

    override var item: T? by mutableStateOf(initialItem)

    init {
        trace()
    }

    override fun invoke(newItem: T?) {
        this.item = newItem
    }
}

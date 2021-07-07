package com.algolia.instantsearch.compose.customdata.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.customdata.QueryRuleCustomDataState

internal class QueryRuleCustomDataStateImpl<T>(initialItem: T?) : QueryRuleCustomDataState<T> {

    override var item: T? by mutableStateOf(initialItem)

    override fun invoke(newItem: T?) {
        this.item = newItem
    }
}

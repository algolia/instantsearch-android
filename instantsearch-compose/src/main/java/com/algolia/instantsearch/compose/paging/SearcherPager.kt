package com.algolia.instantsearch.compose.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

public interface SearcherPager<T : Any> {

    public val flow: Flow<PagingData<T>>

    public fun notifySearcherChanged()
}

package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.event.EventViewModel
import com.algolia.instantsearch.core.event.EventViewModelImpl
import com.algolia.instantsearch.core.item.ItemViewModel


public open class LoadingViewModel(
    loading: Boolean = false
) : ItemViewModel<Boolean>(loading),
    EventViewModel<Unit> by EventViewModelImpl()
package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


public class SearcherScope(val dispatcher: CoroutineDispatcher = defaultDispatcher) : CoroutineScope {

    override val coroutineContext = SupervisorJob() + dispatcher
}
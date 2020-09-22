package com.algolia.instantsearch.helper.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

public class SearcherScope(public val dispatcher: CoroutineDispatcher = defaultDispatcher) : CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
}

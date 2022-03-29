package com.algolia.instantsearch.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

public class SearcherScope(public val dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate) : CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
}

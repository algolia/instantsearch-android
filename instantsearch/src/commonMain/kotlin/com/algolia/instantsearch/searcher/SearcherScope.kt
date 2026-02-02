package com.algolia.instantsearch.searcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

private val defaultDispatcher: CoroutineDispatcher =
    runCatching { Dispatchers.Main.immediate }.getOrElse { Dispatchers.Default }

public class SearcherScope(public val dispatcher: CoroutineDispatcher = defaultDispatcher) : CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
}

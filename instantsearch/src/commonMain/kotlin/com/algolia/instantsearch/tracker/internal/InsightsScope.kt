package com.algolia.instantsearch.tracker.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Default Insights [CoroutineScope].
 *
 * @param dispatcher determines what thread(s) the corresponding coroutine uses for its execution.
 */
internal class InsightsScope(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope {

    override val coroutineContext = SupervisorJob() + dispatcher
}

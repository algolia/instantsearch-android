@file:Suppress("FunctionName")

package com.algolia.instantsearch.core.internal

import kotlinx.coroutines.CancellationException

/**
 * Cancellation due to operation abort.
 */
internal fun AbortException(canceller: String) = CancellationException("$canceller operation abort")

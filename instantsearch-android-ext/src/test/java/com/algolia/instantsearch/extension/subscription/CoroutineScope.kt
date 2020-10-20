package com.algolia.instantsearch.extension.subscription

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope

suspend fun testCoroutineScope(block: CoroutineScope.() -> Unit) {
    coroutineScope {
        block()
        coroutineContext.cancelChildren()
    }
}

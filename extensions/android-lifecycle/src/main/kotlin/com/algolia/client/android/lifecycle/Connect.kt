package com.algolia.client.android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.algolia.client.android.lifecycle.internal.ConnectionLifecycleObserver
import com.algolia.client.android.lifecycle.internal.lifecycleOwner
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.connection.Connection

/**
 * Binds a connection to a [ViewModel] lifecycle.
 */
@ExperimentalInstantSearch
public fun Connection.bind(viewModel: ViewModel) {
    bind(viewModel.lifecycleOwner)
}

/**
 * Binds a connection to a [LifecycleOwner].
 */
@ExperimentalInstantSearch
public fun Connection.bind(lifecycleOwner: LifecycleOwner) {
    bind(lifecycleOwner.lifecycle)
}

/**
 * Binds a connection to a [Lifecycle].
 */
@ExperimentalInstantSearch
public fun Connection.bind(lifecycle: Lifecycle) {
    lifecycle.addObserver(ConnectionLifecycleObserver(this))
}

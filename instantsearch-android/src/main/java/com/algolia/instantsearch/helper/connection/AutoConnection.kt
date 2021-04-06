package com.algolia.instantsearch.helper.connection

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.connection.ConnectionManager

public class AutoConnection(
    lifecycle: Lifecycle,
    public val connectionHandler: ConnectionHandler = ConnectionHandler()
) : LifecycleObserver, ConnectionManager by connectionHandler {

    public constructor(
        lifecycle: Lifecycle,
        vararg connections: Connection
    ) : this(lifecycle, ConnectionHandler(connections.toMutableSet()))

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public fun onDestroy() {
        connectionHandler.clear()
    }
}

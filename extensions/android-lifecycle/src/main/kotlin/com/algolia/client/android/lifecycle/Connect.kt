package com.algolia.client.android.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.algolia.instantsearch.core.connection.Connection

public fun Connection.bind(lifecycleOwner: LifecycleOwner) {
    bind(lifecycleOwner.lifecycle)
}

public fun Connection.bind(lifecycle: Lifecycle) {
    lifecycle.addObserver(LifecycleConnection(this))
}

public class LifecycleConnection(private val connection: Connection) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        connection.connect()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        connection.disconnect()
    }
}

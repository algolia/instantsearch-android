package com.algolia.client.android.lifecycle.internal

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.algolia.instantsearch.core.connection.Connection

/**
 * [Connection] as [LifecycleObserver].
 */
internal class ConnectionLifecycleObserver(private val connection: Connection) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        connection.connect()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        connection.disconnect()
    }
}

package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun LoadingViewModel.connectView(view: LoadingView, connect: Boolean = true): Connection {
   return LoadingConnectionView(this, view).autoConnect(connect)
}
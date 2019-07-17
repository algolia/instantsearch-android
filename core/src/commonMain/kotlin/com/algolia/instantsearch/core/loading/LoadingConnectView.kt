package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.Connection


public fun LoadingViewModel.connectView(view: LoadingView, connect: Boolean = true): Connection {
   return LoadingConnectionView(this, view).apply { if (connect) connect() }
}
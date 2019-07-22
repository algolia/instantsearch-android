package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.Connection


public fun LoadingViewModel.connectionView(view: LoadingView): Connection {
    return LoadingConnectionView(this, view)
}
package com.algolia.instantsearch.core.loading

import com.algolia.instantsearch.core.connection.Connection

public fun LoadingViewModel.connectView(view: LoadingView): Connection {
    return LoadingConnectionView(this, view)
}

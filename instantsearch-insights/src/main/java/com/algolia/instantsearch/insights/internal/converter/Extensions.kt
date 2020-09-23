package com.algolia.instantsearch.insights.internal.converter

import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.UserToken

internal fun Any?.toStringIfRaw(): Any? {
    return when (this) {
        is EventName -> raw
        is UserToken -> raw
        is ObjectID -> raw
        is IndexName -> raw
        is QueryID -> raw
        else -> this
    }
}

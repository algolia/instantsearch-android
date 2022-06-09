package com.algolia.instantsearch.insights.internal.extension

import com.algolia.search.model.insights.InsightsEvent

internal fun InsightsEvent.copy(timestamp: Long?): InsightsEvent {
    return when (this) {
        is InsightsEvent.View -> copy(timestamp = timestamp)
        is InsightsEvent.Click -> copy(timestamp = timestamp)
        is InsightsEvent.Conversion -> copy(timestamp = timestamp)
    }
}


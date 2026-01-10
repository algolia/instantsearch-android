package com.algolia.instantsearch.insights.internal.data.local

import com.algolia.instantsearch.migration2to3.InsightsEvent

internal interface InsightsLocalRepository {

    fun append(event: InsightsEvent)

    fun overwrite(events: List<InsightsEvent>)

    fun read(): List<InsightsEvent>

    fun count(): Int

    fun clear()
}

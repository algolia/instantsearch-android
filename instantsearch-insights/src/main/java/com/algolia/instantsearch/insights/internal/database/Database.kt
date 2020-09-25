package com.algolia.instantsearch.insights.internal.database

import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.InsightsEvent

internal interface Database {

    val indexName: IndexName

    fun append(event: InsightsEvent)

    fun overwrite(events: List<InsightsEvent>)

    fun read(): List<InsightsEvent>

    fun count(): Int

    fun clear()
}

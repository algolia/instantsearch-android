package com.algolia.instantsearch.insights.internal.database

import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.search.model.IndexName

internal interface Database {

    val indexName: IndexName

    fun append(event: EventInternal)

    fun overwrite(events: List<EventInternal>)

    fun read(): List<EventInternal>

    fun count(): Int

    fun clear()
}

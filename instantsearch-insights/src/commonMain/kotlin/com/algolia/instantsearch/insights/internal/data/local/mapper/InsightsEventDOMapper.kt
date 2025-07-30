package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.extension.JsonNonStrict

internal object InsightsEventDOMapper : Mapper<InsightsEventDO, String> {

    override fun map(input: InsightsEventDO): String {
        return JsonNonStrict.encodeToString(InsightsEventDO.serializer(), input)
    }

    override fun unmap(input: String): InsightsEventDO {
        return JsonNonStrict.decodeFromString(InsightsEventDO.serializer(), input)
    }
}

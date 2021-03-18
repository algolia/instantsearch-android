package com.algolia.instantsearch.insights.internal.data.settings

import java.util.UUID

internal interface InsightsSettings {

    var workId: UUID?

    var userToken: String?
}

package com.algolia.instantsearch.insights.event

internal enum class EventType(val key: String) {
    Click("click"),
    View("view"),
    Conversion("conversion");
}

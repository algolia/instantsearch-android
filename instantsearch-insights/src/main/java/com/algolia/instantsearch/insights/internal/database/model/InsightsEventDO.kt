package com.algolia.instantsearch.insights.internal.database.model

internal typealias InsightsEventDO = Map<String, Any?>

internal enum class EventType(val raw: String) {
    Click("click"),
    View("view"),
    Conversion("conversion");
}

internal enum class EventKey(val raw: String) {
    EventType("eventType"),
    EventName("eventName"),
    IndexName("index"),
    UserToken("userToken"),
    Timestamp("timestamp"),
    QueryID("queryID"),
    ObjectIds("objectIDs"),
    Positions("positions"),
    Filters("filters");

    override fun toString(): String {
        return raw
    }
}

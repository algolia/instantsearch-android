package com.algolia.instantsearch.insights.event

public enum class EventKey(val key: String) {
    EventType("eventType"),
    EventName("eventName"),
    IndexName("index"),
    UserToken("userToken"),
    Timestamp("timestamp"),
    QueryId("queryID"),
    ObjectIds("objectIDs"),
    Positions("positions"),
    Filters("filters")
}

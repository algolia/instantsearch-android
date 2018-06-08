package com.algolia.instantsearch.insights


internal enum class EventType(val route: String) {
    Click("click"),
    View("view"),
    Conversion("conversion")
}

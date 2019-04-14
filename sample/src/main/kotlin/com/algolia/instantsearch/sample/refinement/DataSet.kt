package com.algolia.instantsearch.sample.refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import kotlinx.serialization.json.Json


val color = Attribute("color")
val colors = listOf(
    Facet("red", 1),
    Facet("green", 2),
    Facet("blue", 3),
    Facet("yellow", 2)
)
val promotion = Attribute("promotion")
val promotions = listOf(
    Facet("free shipping", 10),
    Facet("on sales", 7),
    Facet("coupon", 3),
    Facet("free return", 10)
)
val category = Attribute("category")
val categories = listOf(
    Facet("shirt", 5),
    Facet("shoe", 5),
    Facet("hat", 4),
    Facet("pants", 10)
)
val responseSearch = ResponseSearch(
    facetsOrNull = mapOf(
        color to colors,
        promotion to promotions,
        category to categories
    )
)
val responseSerialized = Json(encodeDefaults = false).stringify(ResponseSearch.serializer(), responseSearch)
package com.algolia.instantsearch.sample.refinementList

import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.json.Json


class RefinementListClient(
    private val color: Attribute,
    private val promotion: Attribute,
    private val category: Attribute
) {

    private val colors = listOf(
        Facet("red", 1),
        Facet("green", 2),
        Facet("blue", 3),
        Facet("yellow", 2)
    )
    private val promotions = listOf(
        Facet("free shipping", 10),
        Facet("on sales", 7),
        Facet("coupon", 3),
        Facet("free return", 10)
    )
    private val categories = listOf(
        Facet("shirt", 5),
        Facet("shoe", 5),
        Facet("hat", 4),
        Facet("pants", 10)
    )
    private val responseSearch = ResponseSearch(
        facetsOrNull = mapOf(
            color to colors,
            promotion to promotions,
            category to categories
        )
    )
    private val mockEngine = MockEngine {
        MockHttpResponse(
            it.call,
            HttpStatusCode.OK,
            headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString())),
            content = ByteReadChannel(
                Json(encodeDefaults = false).stringify(ResponseSearch.serializer(), responseSearch)
            )
        )
    }

    val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("mock"),
            APIKey("mock"),
            engine = mockEngine
        )
    )
}
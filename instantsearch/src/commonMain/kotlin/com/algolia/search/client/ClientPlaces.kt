package com.algolia.search.client

import com.algolia.client.transport.RequestOptions
import com.algolia.search.model.places.PlacesQuery
import com.algolia.search.model.response.ResponseSearchPlacesMono
import com.algolia.search.model.search.Language

@Deprecated("Places feature is deprecated and not supported with the Kotlin v3 API client.")
public class ClientPlaces {

    public suspend fun searchPlaces(
        language: Language,
        query: PlacesQuery,
        requestOptions: RequestOptions? = null,
    ): ResponseSearchPlacesMono {
        throw UnsupportedOperationException(
            "Places is deprecated and not supported with the Kotlin v3 API client."
        )
    }
}


package com.algolia.instantsearch.insights.internal.data.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ObjectDataDO(
    @SerialName("queryID") val queryID: String? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("discount") val discount: Double? = null,
)

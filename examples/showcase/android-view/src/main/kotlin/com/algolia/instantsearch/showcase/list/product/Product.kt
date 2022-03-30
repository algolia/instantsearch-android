package com.algolia.instantsearch.showcase.list.product

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String,
    val description: String,
    val brand: String? = null,
    val categories: List<String>,
    val type: String,
    val price: Double,
    @SerialName("price_range") val priceRange: String,
    val image: String,
    val url: String,
    @SerialName("free_shipping") val freeShipping: Boolean,
    val rating: Int = 0,
    val popularity: Long,
    override val objectID: ObjectID
) : Indexable

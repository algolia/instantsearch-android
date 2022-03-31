package com.algolia.instantsearch.samples.codex.query.categories.hits

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String,
    val description: String,
    val image: String,
    override val objectID: ObjectID
) : Indexable

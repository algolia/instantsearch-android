package com.algolia.instantsearch.examples.android.codex.suggestions.hits

import com.algolia.instantsearch.core.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String,
    val description: String,
    val image: String,
    override val objectID: String
) : Indexable

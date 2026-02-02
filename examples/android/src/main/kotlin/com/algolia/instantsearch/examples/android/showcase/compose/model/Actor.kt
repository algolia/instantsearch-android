package com.algolia.instantsearch.examples.android.showcase.compose.model

import com.algolia.instantsearch.core.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val name: String,
    override val objectID: String
) : Indexable

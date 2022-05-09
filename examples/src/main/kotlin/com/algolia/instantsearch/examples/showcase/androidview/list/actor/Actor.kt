package com.algolia.instantsearch.examples.showcase.androidview.list.actor

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val name: String,
    override val objectID: ObjectID
) : Indexable

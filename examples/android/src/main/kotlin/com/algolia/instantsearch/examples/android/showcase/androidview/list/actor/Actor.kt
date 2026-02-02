package com.algolia.instantsearch.examples.android.showcase.androidview.list.actor

import com.algolia.instantsearch.core.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val name: String,
    override val objectID: String
) : Indexable

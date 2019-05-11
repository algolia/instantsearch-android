package com.algolia.instantsearch.demo.directory

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.HighlightResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DirectoryHit(
    override val objectID: ObjectID,
    val name: String,
    val type: String,
    @SerialName("_highlightResult")
    val highlightResults: Map<String, HighlightResult>? = null
): Indexable
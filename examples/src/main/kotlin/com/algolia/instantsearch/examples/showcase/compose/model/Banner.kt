package com.algolia.instantsearch.examples.showcase.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val title: String?,
    val banner: String?,
    val link: String
)

package com.algolia.instantsearch.examples.showcase.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val title: String?,
    val banner: String?,
    val link: String,
    val redirect: String?
)


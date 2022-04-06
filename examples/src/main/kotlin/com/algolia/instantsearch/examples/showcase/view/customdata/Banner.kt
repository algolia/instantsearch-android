package com.algolia.instantsearch.examples.showcase.view.customdata

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val title: String?,
    val banner: String?,
    val link: String,
    val redirect: String?
)

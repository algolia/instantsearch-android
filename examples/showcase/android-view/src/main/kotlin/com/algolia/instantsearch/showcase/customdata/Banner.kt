package com.algolia.instantsearch.showcase.customdata

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val title: String?,
    val banner: String?,
    val link: String
)

package com.algolia.instantsearch.samples.showcase.view.customdata

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val title: String?,
    val banner: String?,
    val link: String
)

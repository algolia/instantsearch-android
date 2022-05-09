package com.algolia.instantsearch.examples.showcase.androidview.customdata

import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val title: String?,
    val banner: String?,
    val link: String,
    val redirect: String?
)

package com.algolia.instantsearch.samples.showcase.compose.directory

sealed class DirectoryItem {
    data class Header(val name: String) : DirectoryItem()
    data class Item(val hit: DirectoryHit) : DirectoryItem()
}

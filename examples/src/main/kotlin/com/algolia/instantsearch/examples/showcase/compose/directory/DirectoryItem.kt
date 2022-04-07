package com.algolia.instantsearch.examples.showcase.compose.directory

import com.algolia.instantsearch.examples.showcase.shared.model.DirectoryHit

sealed class DirectoryItem {
    data class Header(val name: String) : DirectoryItem()
    data class Item(val hit: DirectoryHit) : DirectoryItem()
}

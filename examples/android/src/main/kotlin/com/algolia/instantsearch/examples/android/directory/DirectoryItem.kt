package com.algolia.instantsearch.examples.android.directory


sealed class DirectoryItem {

    data class Header(val name: String) : DirectoryItem()

    data class Item(val hit: DirectoryHit) : DirectoryItem()
}

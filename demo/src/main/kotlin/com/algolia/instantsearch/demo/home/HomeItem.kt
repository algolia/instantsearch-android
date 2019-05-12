package com.algolia.instantsearch.demo.home


sealed class HomeItem {

    data class Header(val name: String) : HomeItem()

    data class Item(val hit: HomeHit) : HomeItem()
}
package com.algolia.instantsearch.helper.filter.clear

public class ClearFilterViewModel {
    public val onCleared: MutableList<() -> Unit> = mutableListOf()
}
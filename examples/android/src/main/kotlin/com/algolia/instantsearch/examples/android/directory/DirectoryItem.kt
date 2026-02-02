package com.algolia.instantsearch.examples.android.directory

import androidx.activity.ComponentActivity
import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.highlighting.Highlightable
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

sealed class DirectoryItem {
    data class Header(val name: String) : DirectoryItem()
    data class Item(val hit: DirectoryHit, val dest: KClass<out ComponentActivity>) : DirectoryItem()
}

@Serializable
data class DirectoryHit(
    override val objectID: String,
    val name: String,
    val type: String,
    val index: String? = null,
    override val _highlightResult: JsonObject? = null
) : Indexable, Highlightable {

    val highlightedName
        get() = getHighlight("name", preTag = "<b>", postTag = "</b>")
}

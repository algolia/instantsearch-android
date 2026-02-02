package com.algolia.instantsearch.examples.android.showcase.compose

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.algolia.client.api.SearchClient
import com.algolia.client.configuration.ClientOptions
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.IndexNameHolder
import com.algolia.instantsearch.examples.android.showcase.compose.ui.*
import com.algolia.instantsearch.filter.FilterGroup
import com.algolia.instantsearch.filter.FilterGroupsConverter
import io.ktor.client.plugins.logging.LogLevel

val client = SearchClient(
    appId = "latency",
    apiKey = "1f6fd3a6fb973cb08419fe7d288fa4db",
    options = ClientOptions(
        logLevel = LogLevel.ALL
    )
)


val stubIndexName = "stub"

fun AppCompatActivity.configureSearcher(searcher: IndexNameHolder) {
    searcher.indexName = intent.indexName
}

fun Activity.configureSearcher(searcher: IndexNameHolder) {
    searcher.indexName = intent.indexName
}

val Intent.indexName: String get() = extras!!.getString("indexName")!!

fun <R> configureSearchBox(
    searcher: Searcher<R>,
    searcherQuery: SearchBoxState,
    connections: ConnectionHandler
) {
    SearchBoxConnector(searcher).also {
        connections += it
        connections += it.connectView(searcherQuery)
    }
}

fun Set<FilterGroup<*>>.highlight(
    converter: FilterGroupsConverter<Set<FilterGroup<*>>, String?> = FilterGroupsConverter.SQL.Unquoted,
    colors: Map<String, Color> = mapOf(),
    defaultColor: Color = Color.Black
): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        forEachIndexed { index, group ->
            val color = colors.getOrElse(group.name ?: "") { defaultColor }
            val string = converter(setOf(group)) ?: ""
            withStyle(SpanStyle(color)) {
                append(string)
            }
            if (index < size - 1) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" AND ")
                }
            }
        }
        toAnnotatedString()
    }
}

fun filterColors(vararg attributes: String): Map<String, Color> {
    return attributes.mapIndexed { index, attribute ->
        attribute to when (index) {
            0 -> HoloRedDark
            1 -> HoloBlueDark
            2 -> HoloGreenDark
            3 -> HoloPurple
            else -> Black
        }
    }.toMap()
}

val Activity.showcaseTitle: String
    get() = intent.extras?.getString("name") ?: ""

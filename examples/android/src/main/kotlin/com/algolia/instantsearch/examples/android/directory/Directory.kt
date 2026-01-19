package com.algolia.instantsearch.examples.android.directory

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import com.algolia.client.model.search.SearchResponse
import com.algolia.instantsearch.examples.android.codex.categorieshits.MainActivity as CategoriesHitsCodex
import com.algolia.instantsearch.examples.android.codex.multipleindex.MainActivity as MultipleIndexCodex
import com.algolia.instantsearch.examples.android.codex.multisearch.MainActivity as MultiSearchCodex
import com.algolia.instantsearch.examples.android.codex.suggestions.categories.MainActivity as QuerySuggestionsCategoriesCodex
import com.algolia.instantsearch.examples.android.codex.suggestions.hits.MainActivity as QuerySuggestionsHitsCodex
import com.algolia.instantsearch.examples.android.codex.suggestions.query.MainActivity as QuerySuggestionsCodex
import com.algolia.instantsearch.examples.android.codex.suggestions.recent.MainActivity as QuerySuggestionsRecentCodex
import com.algolia.instantsearch.examples.android.codex.voice.MainActivity as VoiceSearchCodex
import com.algolia.instantsearch.examples.android.guides.compose.ComposeActivity
import com.algolia.instantsearch.examples.android.guides.gettingstarted.GettingStartedGuide
import com.algolia.instantsearch.examples.android.guides.insights.InsightsActivity
import com.algolia.instantsearch.examples.android.showcase.androidview.directory.AndroidViewDirectoryShowcase
import com.algolia.instantsearch.examples.android.showcase.compose.directory.ComposeDirectoryShowcase
import io.ktor.serialization.deserialize
import kotlin.reflect.KClass

val guides = mapOf(
    "guide_getting_started" to GettingStartedGuide::class,
    "guide_insights" to InsightsActivity::class,
    "guide_declarative_ui" to ComposeActivity::class,
    "showcase_imperative_ui" to AndroidViewDirectoryShowcase::class,
    "showcase_declarative_ui" to ComposeDirectoryShowcase::class,
    "codex_categories_hits" to CategoriesHitsCodex::class,
    "codex_multiple_index" to MultipleIndexCodex::class,
    "codex_multiple_index_legacy" to MultiSearchCodex::class,
    "codex_query_suggestions" to QuerySuggestionsCodex::class,
    "codex_query_suggestions_categories" to QuerySuggestionsCategoriesCodex::class,
    "codex_query_suggestions_hits" to QuerySuggestionsHitsCodex::class,
    "codex_query_suggestions_recent" to QuerySuggestionsRecentCodex::class,
    "codex_voice_search" to VoiceSearchCodex::class,
)

internal fun directoryItems(response: SearchResponse, mappings: Map<String, KClass<out ComponentActivity>>) =
    response.hits.deserialize(DirectoryHit.serializer())
        .filter { mappings.containsKey(it.objectID) }
        .groupBy { it.type }
        .toSortedMap()
        .flatMap { (key, value) ->
            listOf(DirectoryItem.Header(key)) + value.map { DirectoryItem.Item(it, mappings.getValue(it.objectID)) }
                .sortedBy { it.hit.objectID.raw }
        }

internal fun Context.navigateTo(item: DirectoryItem.Item) {
    val intent = Intent(this, item.dest.java).apply {
        putExtra("indexName", item.hit.index)
        putExtra("name", item.hit.name)
    }
    startActivity(intent)
}

package com.algolia.instantsearch.examples.android.directory

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
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
import com.algolia.instantsearch.examples.android.guides.querysuggestion.QuerySuggestionActivity
import com.algolia.instantsearch.examples.android.showcase.androidview.directory.AndroidViewDirectoryShowcase
import com.algolia.instantsearch.examples.android.showcase.compose.directory.ComposeDirectoryShowcase
import com.algolia.search.helper.deserialize
import com.algolia.search.model.ObjectID
import com.algolia.search.model.response.ResponseSearch
import kotlin.reflect.KClass

val guides = mapOf(
    ObjectID("guide_getting_started") to GettingStartedGuide::class,
    ObjectID("guide_insights") to InsightsActivity::class,
    ObjectID("guide_declarative_ui") to ComposeActivity::class,
    ObjectID("guide_query_suggestion") to QuerySuggestionActivity::class,
    ObjectID("showcase_imperative_ui") to AndroidViewDirectoryShowcase::class,
    ObjectID("showcase_declarative_ui") to ComposeDirectoryShowcase::class,
    ObjectID("codex_categories_hits") to CategoriesHitsCodex::class,
    ObjectID("codex_multiple_index") to MultipleIndexCodex::class,
    ObjectID("codex_multiple_index_legacy") to MultiSearchCodex::class,
    ObjectID("codex_query_suggestions") to QuerySuggestionsCodex::class,
    ObjectID("codex_query_suggestions_categories") to QuerySuggestionsCategoriesCodex::class,
    ObjectID("codex_query_suggestions_hits") to QuerySuggestionsHitsCodex::class,
    ObjectID("codex_query_suggestions_recent") to QuerySuggestionsRecentCodex::class,
    ObjectID("codex_voice_search") to VoiceSearchCodex::class,
)

internal fun directoryItems(response: ResponseSearch, mappings: Map<ObjectID, KClass<out ComponentActivity>>) =
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

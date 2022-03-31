package com.algolia.instantsearch.samples.directory

import com.algolia.instantsearch.samples.guides.compose.ComposeActivity
import com.algolia.instantsearch.samples.guides.gettingstarted.GettingStartedGuide
import com.algolia.instantsearch.samples.guides.insights.InsightsActivity
import com.algolia.instantsearch.samples.guides.places.PlacesActivity
import com.algolia.instantsearch.samples.guides.querysuggestion.QuerySuggestionActivity
import com.algolia.instantsearch.samples.guides.voice.VoiceSearchActivity
import com.algolia.instantsearch.samples.showcase.compose.directory.ComposeDirectoryShowcase
import com.algolia.instantsearch.samples.showcase.view.directory.AndroidViewDirectoryShowcase
import com.algolia.instantsearch.samples.codex.categorieshits.MainActivity as CategoriesHitsCodex
import com.algolia.instantsearch.samples.codex.multipleindex.MainActivity as MultipleIndexHitsCodex
import com.algolia.search.model.ObjectID

val guides = mapOf(
    ObjectID("guide_getting_started") to GettingStartedGuide::class,
    ObjectID("guide_places") to PlacesActivity::class,
    ObjectID("guide_query_suggestion") to QuerySuggestionActivity::class,
    ObjectID("guide_insights") to InsightsActivity::class,
    ObjectID("guide_declarative_ui") to ComposeActivity::class,
    ObjectID("guide_voice_search") to VoiceSearchActivity::class,
    ObjectID("showcase_imperative_ui") to AndroidViewDirectoryShowcase::class,
    ObjectID("showcase_declarative_ui") to ComposeDirectoryShowcase::class,
    ObjectID("codex_categories_hits") to CategoriesHitsCodex::class,
    ObjectID("codex_multiple_index") to MultipleIndexHitsCodex::class,
    //ObjectID("codex_query_suggestions") to TODO(),
    //ObjectID("codex_query_suggestions_categories") to TODO(),
    //ObjectID("codex_query_suggestions_hits") to TODO(),
    //ObjectID("codex_query_suggestions_recent") to TODO(),
    //ObjectID("codex_voice_search") to TODO(),
)

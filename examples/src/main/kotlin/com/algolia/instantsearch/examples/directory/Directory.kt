package com.algolia.instantsearch.examples.directory

import com.algolia.instantsearch.examples.codex.categorieshits.MainActivity as CategoriesHitsCodex
import com.algolia.instantsearch.examples.codex.multipleindex.MainActivity as MultipleIndexCodex
import com.algolia.instantsearch.examples.codex.suggestions.categories.MainActivity as QuerySuggestionsCategoriesCodex
import com.algolia.instantsearch.examples.codex.suggestions.hits.MainActivity as QuerySuggestionsHitsCodex
import com.algolia.instantsearch.examples.codex.suggestions.query.MainActivity as QuerySuggestionsCodex
import com.algolia.instantsearch.examples.codex.suggestions.recent.MainActivity as QuerySuggestionsRecentCodex
import com.algolia.instantsearch.examples.codex.voice.MainActivity as VoiceSearchCodex
import com.algolia.instantsearch.examples.guides.compose.ComposeActivity
import com.algolia.instantsearch.examples.guides.gettingstarted.GettingStartedGuide
import com.algolia.instantsearch.examples.guides.insights.InsightsActivity
import com.algolia.instantsearch.examples.guides.places.PlacesActivity
import com.algolia.instantsearch.examples.guides.querysuggestion.QuerySuggestionActivity
import com.algolia.instantsearch.examples.guides.voice.VoiceSearchActivity
import com.algolia.instantsearch.examples.showcase.compose.directory.ComposeDirectoryShowcase
import com.algolia.instantsearch.examples.showcase.view.directory.AndroidViewDirectoryShowcase
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
    ObjectID("codex_multiple_index") to MultipleIndexCodex::class,
    ObjectID("codex_query_suggestions") to QuerySuggestionsCodex::class,
    ObjectID("codex_query_suggestions_categories") to QuerySuggestionsCategoriesCodex::class,
    ObjectID("codex_query_suggestions_hits") to QuerySuggestionsHitsCodex::class,
    ObjectID("codex_query_suggestions_recent") to QuerySuggestionsRecentCodex::class,
    ObjectID("codex_voice_search") to VoiceSearchCodex::class,
)

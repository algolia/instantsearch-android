package com.algolia.instantsearch.examples.showcase.compose.directory

import com.algolia.instantsearch.examples.showcase.compose.customdata.QueryRuleCustomDataShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.current.FilterCurrentShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.facet.DynamicFacetShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.facet.FacetListPersistentShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.facet.FacetListShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.list.FilterListFacetShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.list.FilterListNumericShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.list.FilterListTagShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.map.FilterMapShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.numeric.comparison.FilterComparisonShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.range.FilterRangeShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.rating.RatingShowcase
import com.algolia.instantsearch.examples.showcase.compose.filter.toggle.FilterToggleShowcase
import com.algolia.instantsearch.examples.showcase.compose.hierarchical.HierarchicalShowcase
import com.algolia.instantsearch.examples.showcase.compose.highlighting.HighlightingShowcase
import com.algolia.instantsearch.examples.showcase.compose.list.paging.PagingMultipleIndexShowcase
import com.algolia.instantsearch.examples.showcase.compose.list.paging.PagingSingleIndexShowcase
import com.algolia.instantsearch.examples.showcase.compose.loading.LoadingShowcase
import com.algolia.instantsearch.examples.showcase.compose.relateditems.RelatedItemsShowcase
import com.algolia.instantsearch.examples.showcase.compose.search.SearchAsYouTypeShowcase
import com.algolia.instantsearch.examples.showcase.compose.search.SearchOnSubmitShowcase
import com.algolia.instantsearch.examples.showcase.compose.sortby.SortByShowcase
import com.algolia.instantsearch.examples.showcase.compose.stats.StatsShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    // Facet
    ObjectID("dynamic_facets") to DynamicFacetShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_persistent") to FacetListPersistentShowcase::class,
    ObjectID("facet_list_search") to FacetListSearchShowcase::class,
    ObjectID("filter_hierarchical") to HierarchicalShowcase::class,
    // Filter
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("filter_current") to FilterCurrentShowcase::class,
    ObjectID("filter_numeric_range") to FilterRangeShowcase::class,
    ObjectID("filter_numeric_comparison") to FilterComparisonShowcase::class,
    ObjectID("filter_rating") to RatingShowcase::class,
    ObjectID("filter_segment") to FilterMapShowcase::class,
    ObjectID("filter_toggle") to FilterToggleShowcase::class,
    // Filter List
    ObjectID("filter_list_facet") to FilterListFacetShowcase::class,
    ObjectID("filter_list_numeric") to FilterListNumericShowcase::class,
    ObjectID("filter_list_tag") to FilterListTagShowcase::class,
    // Other
    ObjectID("highlighting") to HighlightingShowcase::class,
    ObjectID("loading") to LoadingShowcase::class,
    ObjectID("query_rule_custom_data") to QueryRuleCustomDataShowcase::class,
    ObjectID("stats") to StatsShowcase::class,
    // Paging
    ObjectID("paging_single_index") to PagingSingleIndexShowcase::class,
    ObjectID("paging_multiple_index") to PagingMultipleIndexShowcase::class,
    // Personalization
    ObjectID("personalisation_related_items") to RelatedItemsShowcase::class,
    // Search
    ObjectID("search_as_you_type") to SearchAsYouTypeShowcase::class,
    ObjectID("search_on_submit") to SearchOnSubmitShowcase::class,
    // Sort
    ObjectID("sort_by") to SortByShowcase::class,
)

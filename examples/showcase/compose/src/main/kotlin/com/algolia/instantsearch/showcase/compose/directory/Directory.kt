package com.algolia.instantsearch.showcase.compose.directory

import com.algolia.instantsearch.showcase.compose.customdata.QueryRuleCustomDataShowcase
import com.algolia.instantsearch.showcase.compose.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.showcase.compose.filter.current.FilterCurrentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.DynamicFacetShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListPersistentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListAllShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListFacetShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListNumericShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListTagShowcase
import com.algolia.instantsearch.showcase.compose.filter.map.FilterMapShowcase
import com.algolia.instantsearch.showcase.compose.filter.numeric.comparison.FilterComparisonShowcase
import com.algolia.instantsearch.showcase.compose.filter.range.FilterRangeShowcase
import com.algolia.instantsearch.showcase.compose.filter.rating.RatingShowcase
import com.algolia.instantsearch.showcase.compose.filter.toggle.FilterToggleShowcase
import com.algolia.instantsearch.showcase.compose.hierarchical.HierarchicalShowcase
import com.algolia.instantsearch.showcase.compose.highlighting.HighlightingShowcase
import com.algolia.instantsearch.showcase.compose.list.merged.MergedListShowcase
import com.algolia.instantsearch.showcase.compose.list.paging.PagingMultipleIndexShowcase
import com.algolia.instantsearch.showcase.compose.list.paging.PagingSingleIndexShowcase
import com.algolia.instantsearch.showcase.compose.loading.LoadingShowcase
import com.algolia.instantsearch.showcase.compose.relateditems.RelatedItemsShowcase
import com.algolia.instantsearch.showcase.compose.search.SearchAsYouTypeShowcase
import com.algolia.instantsearch.showcase.compose.search.SearchAutoCompleteShowcase
import com.algolia.instantsearch.showcase.compose.search.SearchOnSubmitShowcase
import com.algolia.instantsearch.showcase.compose.sortby.SortByShowcase
import com.algolia.instantsearch.showcase.compose.stats.StatsShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to FilterCurrentShowcase::class,
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_search") to FacetListSearchShowcase::class,
    ObjectID("facet_list_persistent") to FacetListPersistentShowcase::class,
    ObjectID("filter_list_all") to FilterListAllShowcase::class,
    ObjectID("filter_list_facet") to FilterListFacetShowcase::class,
    ObjectID("filter_list_numeric") to FilterListNumericShowcase::class,
    ObjectID("filter_list_tag") to FilterListTagShowcase::class,
    ObjectID("filter_segment") to FilterMapShowcase::class,
    ObjectID("filter_numeric_comparison") to FilterComparisonShowcase::class,
    ObjectID("filter_numeric_range") to FilterRangeShowcase::class,
    ObjectID("filter_rating") to RatingShowcase::class,
    ObjectID("filter_toggle") to FilterToggleShowcase::class,
    ObjectID("filter_hierarchical") to HierarchicalShowcase::class,
    ObjectID("highlighting") to HighlightingShowcase::class,
    ObjectID("merged_list") to MergedListShowcase::class,
    ObjectID("paging_single_index") to PagingSingleIndexShowcase::class,
    ObjectID("paging_multiple_index") to PagingMultipleIndexShowcase::class,
    ObjectID("loading") to LoadingShowcase::class,
    ObjectID("personalisation_related_items") to RelatedItemsShowcase::class,
    ObjectID("search_as_you_type") to SearchAsYouTypeShowcase::class,
    ObjectID("search_on_submit") to SearchOnSubmitShowcase::class,
    ObjectID("search_auto_complete_text_view") to SearchAutoCompleteShowcase::class,
    ObjectID("sort_by") to SortByShowcase::class,
    ObjectID("stats") to StatsShowcase::class,
    ObjectID("query_rule_custom_data") to QueryRuleCustomDataShowcase::class,
    ObjectID("dynamic_facets") to DynamicFacetShowcase::class,
)

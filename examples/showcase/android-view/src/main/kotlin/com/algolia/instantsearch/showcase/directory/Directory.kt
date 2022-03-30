package com.algolia.instantsearch.showcase.directory

import com.algolia.instantsearch.showcase.customdata.QueryRuleCustomDataShowcase
import com.algolia.instantsearch.showcase.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.showcase.filter.current.ShowcaseFilterCurrent
import com.algolia.instantsearch.showcase.filter.facet.dynamic.DynamicFacetShowcase
import com.algolia.instantsearch.showcase.filter.facet.FacetListPersistentShowcase
import com.algolia.instantsearch.showcase.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.showcase.filter.facet.FacetListShowcase
import com.algolia.instantsearch.showcase.filter.list.FilterListAllShowcase
import com.algolia.instantsearch.showcase.filter.list.FilterListFacetShowcase
import com.algolia.instantsearch.showcase.filter.list.FilterListNumericShowcase
import com.algolia.instantsearch.showcase.filter.list.FilterListTagShowcase
import com.algolia.instantsearch.showcase.filter.map.FilterMapShowcase
import com.algolia.instantsearch.showcase.filter.numeric.comparison.FilterComparisonShowcase
import com.algolia.instantsearch.showcase.filter.range.FilterRangeShowcase
import com.algolia.instantsearch.showcase.filter.rating.RatingShowcase
import com.algolia.instantsearch.showcase.filter.toggle.FilterToggleShowcase
import com.algolia.instantsearch.showcase.hierarchical.HierarchicalShowcase
import com.algolia.instantsearch.showcase.highlighting.HighlightingShowcase
import com.algolia.instantsearch.showcase.list.merged.MergedListShowcase
import com.algolia.instantsearch.showcase.list.paging.PagingMultipleIndexShowcase
import com.algolia.instantsearch.showcase.list.paging.PagingSingleIndexShowcase
import com.algolia.instantsearch.showcase.loading.LoadingShowcase
import com.algolia.instantsearch.showcase.relateditems.RelatedItemsShowcase
import com.algolia.instantsearch.showcase.search.SearchAsYouTypeShowcase
import com.algolia.instantsearch.showcase.search.SearchAutoCompleteTextView
import com.algolia.instantsearch.showcase.search.SearchOnSubmitShowcase
import com.algolia.instantsearch.showcase.sortby.SortByShowcase
import com.algolia.instantsearch.showcase.stats.StatsShowcase
import com.algolia.instantsearch.showcase.suggestion.QuerySuggestionShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to ShowcaseFilterCurrent::class,
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("filter_toggle") to FilterToggleShowcase::class,
    ObjectID("filter_segment") to FilterMapShowcase::class,
    ObjectID("filter_list_all") to FilterListAllShowcase::class,
    ObjectID("filter_list_numeric") to FilterListNumericShowcase::class,
    ObjectID("filter_list_facet") to FilterListFacetShowcase::class,
    ObjectID("filter_list_tag") to FilterListTagShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_persistent") to FacetListPersistentShowcase::class,
    ObjectID("facet_list_search") to FacetListSearchShowcase::class,
    ObjectID("filter_numeric_comparison") to FilterComparisonShowcase::class,
    ObjectID("filter_numeric_range") to FilterRangeShowcase::class,
    ObjectID("search_on_submit") to SearchOnSubmitShowcase::class,
    ObjectID("search_as_you_type") to SearchAsYouTypeShowcase::class,
    ObjectID("search_auto_complete_text_view") to SearchAutoCompleteTextView::class,
    ObjectID("paging_single_searcher") to PagingSingleIndexShowcase::class,
    ObjectID("sort_by") to SortByShowcase::class,
    ObjectID("stats") to StatsShowcase::class,
    ObjectID("loading") to LoadingShowcase::class,
    ObjectID("paging_single_index") to PagingSingleIndexShowcase::class,
    ObjectID("paging_multiple_index") to PagingMultipleIndexShowcase::class,
    ObjectID("filter_hierarchical") to HierarchicalShowcase::class,
    ObjectID("merged_list") to MergedListShowcase::class,
    ObjectID("highlighting") to HighlightingShowcase::class,
    ObjectID("personalisation_related_items") to RelatedItemsShowcase::class,
    ObjectID("query_rule_custom_data") to QueryRuleCustomDataShowcase::class,
    ObjectID("filter_rating") to RatingShowcase::class,
    ObjectID("dynamic_facets") to DynamicFacetShowcase::class,
    ObjectID("query_suggestions") to QuerySuggestionShowcase::class,
)

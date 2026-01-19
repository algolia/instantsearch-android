package com.algolia.instantsearch.examples.android.showcase.androidview.directory

import com.algolia.instantsearch.examples.android.showcase.androidview.customdata.QueryRuleCustomDataShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.current.ShowcaseFilterCurrent
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.facet.FacetListPersistentShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.facet.FacetListShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.facet.dynamic.DynamicFacetShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.list.FilterListFacetShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.list.FilterListNumericShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.list.FilterListTagShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.map.FilterMapShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.numeric.comparison.FilterComparisonShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.range.FilterRangeShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.rating.RatingShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.filter.toggle.FilterToggleShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.hierarchical.HierarchicalShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.highlighting.HighlightingShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.list.paging.PagingMultipleIndexShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.list.paging.PagingSingleIndexShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.loading.LoadingShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.relateditems.RelatedItemsShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.search.SearchAsYouTypeShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.search.SearchOnSubmitShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.sortby.SortByShowcase
import com.algolia.instantsearch.examples.android.showcase.androidview.stats.StatsShowcase

val showcases = mapOf(
    // Facet
    "dynamic_facets" to DynamicFacetShowcase::class,
    "facet_list" to FacetListShowcase::class,
    "facet_list_persistent" to FacetListPersistentShowcase::class,
    "facet_list_search" to FacetListSearchShowcase::class,
    "filter_hierarchical" to HierarchicalShowcase::class,
    // Filter
    "filter_clear" to FilterClearShowcase::class,
    "filter_current" to ShowcaseFilterCurrent::class,
    "filter_numeric_range" to FilterRangeShowcase::class,
    "filter_numeric_comparison" to FilterComparisonShowcase::class,
    "filter_rating" to RatingShowcase::class,
    "filter_segment" to FilterMapShowcase::class,
    "filter_toggle" to FilterToggleShowcase::class,
    // Filter List
    "filter_list_facet" to FilterListFacetShowcase::class,
    "filter_list_numeric" to FilterListNumericShowcase::class,
    "filter_list_tag" to FilterListTagShowcase::class,
    // Other
    "highlighting" to HighlightingShowcase::class,
    "loading" to LoadingShowcase::class,
    "query_rule_custom_data" to QueryRuleCustomDataShowcase::class,
    "stats" to StatsShowcase::class,
    // Paging
    "paging_single_index" to PagingSingleIndexShowcase::class,
    "paging_multiple_index" to PagingMultipleIndexShowcase::class,
    // Personalization
    "personalisation_related_items" to RelatedItemsShowcase::class,
    // Search
    "search_as_you_type" to SearchAsYouTypeShowcase::class,
    "search_on_submit" to SearchOnSubmitShowcase::class,
    // Sort
    "sort_by" to SortByShowcase::class,
)

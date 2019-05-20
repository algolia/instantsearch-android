@file:JvmName("HomeDemoKt")

package com.algolia.instantsearch.demo.home

import com.algolia.instantsearch.demo.filter.facet.FacetListDemo
import com.algolia.instantsearch.demo.filter.facet.FacetListSearchDemo
import com.algolia.instantsearch.demo.filter.list.FilterListAllDemo
import com.algolia.instantsearch.demo.filter.list.FilterListFacetDemo
import com.algolia.instantsearch.demo.filter.list.FilterListNumericDemo
import com.algolia.instantsearch.demo.filter.list.FilterListTagDemo
import com.algolia.instantsearch.demo.filter.numeric.comparison.FilterComparisonDemo
import com.algolia.instantsearch.demo.filter.segment.FilterSegmentDemo
import com.algolia.instantsearch.demo.filter.toggle.FilterToggleDefaultDemo
import com.algolia.instantsearch.demo.filter.toggle.FilterToggleDemo
import com.algolia.instantsearch.demo.list.paging.PagingSingleIndexDemo
import com.algolia.instantsearch.demo.searchbox.SearchOnSubmitDemo
import com.algolia.search.model.ObjectID


val homeActivities = mapOf(
    ObjectID("filter_toggle") to FilterToggleDemo::class,
    ObjectID("filter_toggle_default") to FilterToggleDefaultDemo::class,
    ObjectID("filter_segment") to FilterSegmentDemo::class,
    ObjectID("filter_list_all") to FilterListAllDemo::class,
    ObjectID("filter_list_numeric") to FilterListNumericDemo::class,
    ObjectID("filter_list_facet") to FilterListFacetDemo::class,
    ObjectID("filter_list_tag") to FilterListTagDemo::class,
    ObjectID("facet_list") to FacetListDemo::class,
    ObjectID("facet_list_search") to FacetListSearchDemo::class,
    ObjectID("paging_single_searcher") to PagingSingleIndexDemo::class,
    ObjectID("filter_numeric_comparison") to FilterComparisonDemo::class,
    ObjectID("paging_single_searcher") to PagingSingleIndexDemo::class,
    ObjectID("search_on_submit") to SearchOnSubmitDemo::class
)
package com.algolia.instantsearch.demo.home

import com.algolia.instantsearch.demo.filter.facet.FacetListDemo
import com.algolia.instantsearch.demo.filter.list.FilterListDemo
import com.algolia.instantsearch.demo.filter.segment.FilterSegmentDemo
import com.algolia.instantsearch.demo.filter.toggle.FilterToggleDemo
import com.algolia.search.model.ObjectID


val homeType = mapOf(
    ObjectID("0") to FilterToggleDemo::class,
    ObjectID("1") to FilterSegmentDemo::class,
    ObjectID("2") to FilterListDemo::class,
    ObjectID("3") to FacetListDemo::class
)
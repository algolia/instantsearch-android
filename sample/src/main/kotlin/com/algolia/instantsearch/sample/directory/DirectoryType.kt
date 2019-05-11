package com.algolia.instantsearch.sample.directory

import com.algolia.instantsearch.sample.selectable.facet.SelectableFacetsDemo
import com.algolia.instantsearch.sample.selectable.filter.SelectableFiltersDemo
import kotlin.reflect.KClass


enum class DirectoryType(val clazz: KClass<*>) {
    SelectableFacets(SelectableFacetsDemo::class),
    SelectableFilter(SelectableFiltersDemo::class)
}
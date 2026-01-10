package com.algolia.instantsearch.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.migration2to3.Facet
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
public typealias FacetListItem = SelectableItem<Facet>

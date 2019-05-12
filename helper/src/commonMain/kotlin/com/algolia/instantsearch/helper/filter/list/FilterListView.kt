package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListView
import com.algolia.search.model.filter.Filter


interface FilterListView<T: Filter>: SelectableListView<T> {

    interface Facet : FilterListView<Filter.Facet>
    interface Tag : FilterListView<Filter.Tag>
    interface Numeric : FilterListView<Filter.Numeric>
    interface All : FilterListView<Filter>
}
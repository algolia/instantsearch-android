package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListView
import com.algolia.search.model.filter.Filter


public interface FilterListView<T: Filter>: SelectableListView<T> {

    public interface Facet : FilterListView<Filter.Facet>
    public interface Tag : FilterListView<Filter.Tag>
    public interface Numeric : FilterListView<Filter.Numeric>
    public interface All : FilterListView<Filter>
}
package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.SelectableListView
import com.algolia.search.model.filter.Filter

/**
 * A View that can display a list of filters, and might allow the user to select them.
 */
public interface FilterListView<T : Filter> : SelectableListView<T> {

    /**
     * A [FilterListView] that can display a list of facet filters.
     */
    public interface Facet : FilterListView<Filter.Facet>

    /**
     * A [FilterListView] that can display a list of tag filters.
     */
    public interface Tag : FilterListView<Filter.Tag>

    /**
     * A [FilterListView] that can display a list of numeric filters.
     */
    public interface Numeric : FilterListView<Filter.Numeric>

    /**
     * A [FilterListView] that can display a list of all kind of filters.
     */
    public interface All : FilterListView<Filter>
}
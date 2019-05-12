package selectable.list

import com.algolia.search.model.filter.Filter

interface SelectableFilterListView<T: Filter>: SelectableListView<T> {

    interface Facet : SelectableFilterListView<Filter.Facet>
    interface Tag : SelectableFilterListView<Filter.Tag>
    interface Numeric : SelectableFilterListView<Filter.Numeric>
    interface All : SelectableFilterListView<Filter>
}
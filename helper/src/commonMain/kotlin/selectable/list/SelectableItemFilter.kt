package selectable.list

import com.algolia.search.model.filter.Filter


public typealias SelectableFilterFacet = SelectableItem<Filter.Facet>
public typealias SelectableFilterNumeric = SelectableItem<Filter.Numeric>
public typealias SelectableFilterTag = SelectableItem<Filter.Tag>
public typealias SelectableFilter = SelectableItem<Filter>
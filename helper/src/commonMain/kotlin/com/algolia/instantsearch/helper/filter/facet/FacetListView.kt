package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectableListView
import com.algolia.search.model.search.Facet

/**
 * A View that can display a list of facets, and might allow the user to select them.
 */
public interface FacetListView : SelectableListView<Facet>
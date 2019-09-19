package com.algolia.instantsearch.helper.filter.map

import com.algolia.instantsearch.core.selectable.map.SelectableMapView


/**
 * A View that can display a map of filters, and might allow the user to select one.
 */
public typealias FilterMapView = SelectableMapView<Int, String>
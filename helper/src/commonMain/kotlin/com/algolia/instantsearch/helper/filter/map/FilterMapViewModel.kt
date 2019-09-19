package com.algolia.instantsearch.helper.filter.map

import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import com.algolia.search.model.filter.Filter


//TODO (JavaDX) Find a way to refactor as subclass without TestFilterMapConnectFilterState failing
public typealias FilterMapViewModel = SelectableMapViewModel<Int, Filter>
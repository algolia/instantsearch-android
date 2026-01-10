package com.algolia.instantsearch.filter.current

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.migration2to3.Filter

public interface FilterCurrentPresenter : Presenter<Map<FilterAndID, Filter>, List<Pair<FilterAndID, String>>>

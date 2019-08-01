package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.Presenter
import com.algolia.search.model.filter.Filter


public interface FilterCurrentPresenter : Presenter<Map<FilterAndID, Filter>, List<Pair<FilterAndID, String>>>
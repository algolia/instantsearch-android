package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.Presenter


public interface FilterCurrentPresenter : Presenter<Set<FilterAndID>, List<Pair<FilterAndID, String>>>
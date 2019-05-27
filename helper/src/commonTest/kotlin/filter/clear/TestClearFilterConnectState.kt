package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import shouldEqual
import kotlin.test.Test

class TestClearFilterConnectState {
    private val color = Attribute("color")
    private val facet = Filter.Facet(color, 0)
    private val group = FilterGroupID.And(color)

    @Test
    fun connectShouldClearFiltersOnClear() {
        val viewModel = ClearFilterViewModel()
        val view = MockClearFilterView()
        viewModel.connectView(view)
        val filterState = FilterState().apply { add(group, facet) }

        viewModel.connectState(filterState)
        view.onClick?.invoke()
        filterState.getFilters() shouldEqual setOf()
    }

    @Test
    fun connectShouldClearQueryIfSpecified() {
        val viewModel = ClearFilterViewModel()
        val view = MockClearFilterView()
        val searcherWithQuery = object : Searcher {
            override val coroutineScope: CoroutineScope = SearcherScope()
            override val dispatcher: CoroutineDispatcher = Dispatchers.Main
            val query: Query = Query("foo")
            override fun setQuery(text: String?) {
                query.query = text
            }

            override fun search(): Job = Job()
            override fun cancel() = Unit
        }
        val filterState = FilterState()

        viewModel.connectView(view)
        viewModel.connectState(filterState, searcherWithQuery)
        view.onClick?.invoke()
        searcherWithQuery.query.query shouldEqual ""
    }
}
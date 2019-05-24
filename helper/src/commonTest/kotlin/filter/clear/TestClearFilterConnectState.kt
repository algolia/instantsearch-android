package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.Searcher
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Query
import kotlinx.coroutines.CoroutineDispatcher
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
        val searcherQuery = object : Searcher {
            val query: Query = Query()
            override val dispatcher: CoroutineDispatcher = Dispatchers.Main
            override fun setQuery(text: String?) {
                query.query = text
            }

            override fun search(): Job = Job()
            override fun cancel() = Unit
        }
        viewModel.connectView(view)
        val filterState = FilterState()
        viewModel.connectState(filterState, searcherQuery)
        view.onClick?.invoke()
        searcherQuery.query.query shouldEqual ""
    }
}
package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Query
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
        val query = Query("foo")
        viewModel.connectView(view)
        val filterState = FilterState()
        viewModel.connectState(filterState, query)
        view.onClick?.invoke()
        query.query shouldEqual ""
    }
}
package filter.clear

import com.algolia.instantsearch.helper.filter.clear.ClearFilterViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
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

        viewModel.connectFilterState(filterState)
        view.onClick?.invoke()
        filterState.getFilters() shouldEqual setOf()
    }
}
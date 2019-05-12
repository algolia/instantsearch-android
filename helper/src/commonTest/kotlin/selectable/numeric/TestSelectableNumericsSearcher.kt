package selectable.numeric

import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import com.algolia.search.model.response.ResponseSearch
import filter.FilterGroupID
import filter.FilterState
import mockClient
import searcher.SearcherSingleIndex
import shouldEqual
import kotlin.test.Test


class TestSelectableNumericsSearcher {

    private val price = Attribute("price")
    private val filter = Filter.Numeric(price, NumericOperator.Greater, 5)
    private val response = ResponseSearch()
    private val client = mockClient(response, ResponseSearch.serializer())
    private val index = client.initIndex(IndexName("index"))
    private val selections = setOf(filter)
    private val groupID = FilterGroupID.And(price)
    private val filterState = FilterState(numericGroups = mutableMapOf(groupID to setOf(filter)))

    @Test
    fun connectShouldUpdateSelectionsWithFilterState() {
        val searcher = SearcherSingleIndex(index, filterState = filterState)
        val viewModel = SelectableNumericsViewModel()

        viewModel.connectSearcher(price, searcher)
        viewModel.selections shouldEqual selections
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = SelectableNumericsViewModel()

        viewModel.connectSearcher(price, searcher)
        viewModel.computeSelections(filter)
        searcher.filterState.filters shouldEqual filterState.filters
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val searcher = SearcherSingleIndex(index)
        val viewModel = SelectableNumericsViewModel()

        viewModel.connectSearcher(price, searcher)
        searcher.filterState.notify { add(groupID, filter) }
        viewModel.selections shouldEqual selections
    }
}
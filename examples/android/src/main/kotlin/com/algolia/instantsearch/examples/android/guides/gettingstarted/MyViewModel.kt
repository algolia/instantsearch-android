package com.algolia.instantsearch.examples.android.guides.gettingstarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingConfig
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.filterstate.connectPaginator
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.examples.android.guides.model.Product
import com.algolia.instantsearch.filter.facet.DefaultFacetListPresenter
import com.algolia.instantsearch.filter.facet.FacetListConnector
import com.algolia.instantsearch.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName

class MyViewModel : ViewModel() {

    val searcher = HitsSearcher(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        indexName = IndexName("instant_search")
    )
    val paginator = Paginator(
        searcher = searcher,
        pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false),
        transformer = { hit -> hit.deserialize(Product.serializer()) }
    )
    val searchBox = SearchBoxConnector(searcher, searchOnQueryUpdate = false)
    val stats = StatsConnector(searcher)

    val filterState = FilterState()
    val facetList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = String("categories"),
        selectionMode = SelectionMode.Single
    )
    val facetPresenter = DefaultFacetListPresenter(
        sortBy = listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined),
        limit = 100
    )
    val connection = ConnectionHandler(searchBox, stats, facetList)

    init {
        connection += searchBox.connectPaginator(paginator)
        connection += searcher.connectFilterState(filterState)
        connection += filterState.connectPaginator(paginator)
    }


    private val _displayFilters = MutableLiveData<Unit>()
    val displayFilters: LiveData<Unit> get() = _displayFilters

    fun navigateToFilters() {
        _displayFilters.value = Unit
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.clear()
    }
}

package com.algolia.instantsearch.showcase.hierarchical

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.hierarchical.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.HeaderFilterBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseHierarchicalBinding
import com.algolia.search.model.Attribute

class HierarchicalShowcase : AppCompatActivity() {

    private val hierarchicalCategory = Attribute("hierarchicalCategories")
    private val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.lvl0")
    private val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.lvl1")
    private val hierarchicalCategoryLvl2 = Attribute("$hierarchicalCategory.lvl2")
    private val hierarchicalAttributes = listOf(
        hierarchicalCategoryLvl0,
        hierarchicalCategoryLvl1,
        hierarchicalCategoryLvl2
    )
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterState = FilterState()
    private val separator = " > "
    private val hierarchical = HierarchicalConnector(
        searcher = searcher,
        attribute = hierarchicalCategory,
        filterState = filterState,
        hierarchicalAttributes = hierarchicalAttributes,
        separator = separator
    )
    private val connection = ConnectionHandler(
        hierarchical,
        searcher.connectFilterState(filterState)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseHierarchicalBinding.inflate(layoutInflater)
        val headerBinding = HeaderFilterBinding.bind(binding.headerFilter.root)
        setContentView(binding.root)

        val view = HierarchicalAdapter()
        connection += hierarchical.connectView(view, HierarchicalPresenterImpl(separator))

        configureRecyclerView(binding.hits, view)
        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, headerBinding.filtersTextView, hierarchicalCategory)
        onErrorThenUpdateFiltersText(searcher, headerBinding.filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, headerBinding.nbHits, connection)
        onClearAllThenClearFilters(filterState, headerBinding.filtersClearAll, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}

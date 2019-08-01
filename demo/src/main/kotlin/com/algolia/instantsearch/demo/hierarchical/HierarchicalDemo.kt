package com.algolia.instantsearch.demo.hierarchical

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.configureSearcher
import com.algolia.instantsearch.demo.configureToolbar
import com.algolia.instantsearch.demo.stubIndex
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.helper.hierarchical.HierarchicalPresenterImpl
import com.algolia.instantsearch.helper.hierarchical.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_hierarchical.*


class HierarchicalDemo : AppCompatActivity() {

    private val hierarchicalCategory = Attribute("hierarchicalCategories")
    private val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.lvl0")
    private val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.lvl1")
    private val hierarchicalCategoryLvl2 = Attribute("$hierarchicalCategory.lvl2")
    private val hierarchicalAttributes = listOf(
        hierarchicalCategoryLvl0,
        hierarchicalCategoryLvl1,
        hierarchicalCategoryLvl2
    )
    private val searcher = SearcherSingleIndex(stubIndex)
    private val filterState = FilterState()
    private val separator = " > "
    private val hierarchical = HierarchicalConnector(searcher, filterState, hierarchicalAttributes, separator)
    private val connection = ConnectionHandler(hierarchical, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_hierarchical)

        val view = HierarchicalAdapter()

        connection += hierarchical.connectView(view, HierarchicalPresenterImpl(separator))

        list.let {
            it.adapter = view
            it.layoutManager = LinearLayoutManager(this)
        }

        configureToolbar(toolbar)
        configureSearcher(searcher)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}
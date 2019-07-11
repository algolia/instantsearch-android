package com.algolia.instantsearch.demo.hierarchical

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.tree.connectView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.configureSearcher
import com.algolia.instantsearch.demo.configureToolbar
import com.algolia.instantsearch.demo.stubIndex
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.hierarchical.*
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.demo_facet_list.*
import kotlinx.android.synthetic.main.demo_hierarchical.*
import kotlinx.android.synthetic.main.demo_hierarchical.toolbar


class HierarchicalDemo : AppCompatActivity() {

    private val hierarchicalCategory = Attribute("hierarchicalCategories")
    private val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.lvl0")
    private val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.lvl1")
    private val hierarchicalCategoryLvl2 = Attribute("$hierarchicalCategory.lvl2")
    private val order = listOf(
        hierarchicalCategoryLvl0,
        hierarchicalCategoryLvl1,
        hierarchicalCategoryLvl2
    )

    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_hierarchical)

        val separator = " > "
        val filterState = FilterState()
        val viewModel = HierarchicalViewModel(order, separator)
        val view = HierarchicalAdapter()

        searcher.connectFilterState(filterState)
        viewModel.connectSearcher(searcher)
        viewModel.connectFilterState(filterState, hierarchicalCategory)
        viewModel.connectView(view, HierarchicalPresenterImpl(separator))

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
    }
}
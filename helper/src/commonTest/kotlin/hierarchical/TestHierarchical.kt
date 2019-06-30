package hierarchical

import blocking
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.hierarchical.HierarchicalViewModel
import com.algolia.instantsearch.helper.hierarchical.connectFilterState
import com.algolia.instantsearch.helper.hierarchical.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.Query
import io.ktor.client.features.logging.LogLevel
import shouldEqual
import kotlin.test.Test


class TestHierarchical {

    val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("latency"),
            APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
            logLevel = LogLevel.NONE
        )
    )
    val index = client.initIndex(IndexName("mobile_demo_hierarchical"))

    private val color = Attribute("color")
    private val hierarchicalCategory = Attribute("hierarchicalCategories")
    private val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.lvl0")
    private val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.lvl1")
    private val hierarchicalCategoryLvl2 = Attribute("$hierarchicalCategory.lvl2")

    private val category1 = "Category1"
    private val category2 = "Category2"
    private val category3 = "Category3"
    private val category3Sub1 = "Category3 > SubCategory1"
    private val category3Sub2 = "Category3 > SubCategory2"
    private val category3Sub1Sub1 = "Category3 > SubCategory1 > SubSubCategory1"
    private val category3Sub1Sub2 = "Category3 > SubCategory1 > SubSubCategory2"
    private val category3Sub2Sub1 = "Category3 > SubCategory2 > SubSubCategory1"
    private val category3Sub2Sub2 = "Category3 > SubCategory2 > SubSubCategory2"
    private val hierarchicalAttributes = listOf(
        hierarchicalCategoryLvl0,
        hierarchicalCategoryLvl1,
        hierarchicalCategoryLvl2
    )

    @Test
    fun test() {
        blocking {
            val filterState = FilterState()
            val query = Query(facets = hierarchicalAttributes.toSet())
            val searcher = SearcherSingleIndex(index, query)
            val separator = " > "
            val viewModel = HierarchicalViewModel(hierarchicalAttributes, separator)

            searcher.connectFilterState(filterState)
            viewModel.connectSearcher(searcher)
            viewModel.connectFilterState(filterState, hierarchicalCategory)

            searcher.search()

            viewModel.computeSelections(category3Sub2Sub2)

            searcher.sequencer.currentOperation.join()

            viewModel.item shouldEqual listOf(
                listOf(Facet(category3, 4), Facet(category2, 2), Facet(category1, 1)),
                listOf(Facet(category3Sub2, 2), Facet(category3Sub1, 2)),
                listOf(Facet(category3Sub2Sub1, 1), Facet(category3Sub2Sub2, 1))
            )
        }
    }
}
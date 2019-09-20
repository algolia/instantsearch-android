package dx

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightToken
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.number.*
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.core.searchbox.SearchBoxView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.core.searcher.*
import com.algolia.instantsearch.core.selectable.SelectableItemView
import com.algolia.instantsearch.core.selectable.SelectableItemViewModel
import com.algolia.instantsearch.core.selectable.connectView
import com.algolia.instantsearch.core.selectable.list.*
import com.algolia.instantsearch.core.selectable.map.SelectableMapView
import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import com.algolia.instantsearch.core.selectable.map.connectView
import com.algolia.instantsearch.core.subscription.Subscription
import com.algolia.instantsearch.core.subscription.send
import com.algolia.instantsearch.core.tree.*
import com.algolia.instantsearch.helper.attribute.AttributeMatchAndReplace
import com.algolia.instantsearch.helper.attribute.AttributePresenterImpl
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.clear.*
import com.algolia.instantsearch.helper.filter.current.*
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.list.FilterListView
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.map.FilterMapViewModel
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectFilterState
import com.algolia.instantsearch.helper.filter.numeric.comparison.setBoundsFromFacetStatsInt
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.connectFilterState
import com.algolia.instantsearch.helper.hierarchical.*
import com.algolia.instantsearch.helper.loading.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.FacetStats
import org.junit.AfterClass
import kotlin.test.Test

@Suppress(
    "UNUSED_VARIABLE",
    "VARIABLE_WITH_REDUNDANT_INITIALIZER"
) // Variables are created just to assess DX
internal class KotlinDX {

    //TODO: Maybe it's worth introducing mocked objects to avoid cumbersome nullability
    private var searcherSingleIndex: SearcherSingleIndex? = null
    private var searcherMultipleIndex: SearcherMultipleIndex? = null
    private var searcherForFacets: SearcherForFacets? = null
    private var searchers: List<Searcher<out Any>?> =
        listOf(searcherSingleIndex, searcherMultipleIndex, searcherForFacets)

    private var filterState = FilterState()
    private var attribute = Attribute("attribute")
    private val groupIDs = listOf(FilterGroupID())
    private val filterFacet = Filter.Facet(attribute, "foo", 0, false)
    private val facet = Facet("facet", 42)
    private val facets = listOf(facet)
    private val facetStats = mapOf(attribute to FacetStats(0f, 2f, 1f, 3f))

    //region Core
    @Test
    fun connection() {
        val handler = ConnectionHandler()
        handler += ConnectionImpl()
        handler.disconnect()
    }

    @Test
    fun highlighting() {
        // Token
        val (content, highlighted) = HighlightToken("foo")

        // Tokenizer
        var tokenizer = HighlightTokenizer()
        val preTag = DefaultPreTag
        val postTag = DefaultPostTag
        tokenizer = HighlightTokenizer(preTag)
        tokenizer = HighlightTokenizer(preTag, postTag)
        tokenizer.preTag
        tokenizer.postTag

        // String
        val invoke = tokenizer("foo<b>bar</b>")
        invoke.tokens
        invoke.highlightedTokens
        invoke.original
    }

    @Test
    fun hits() {
        val hitsView = object : HitsView<ResponseSearch.Hit> {
            override fun setHits(hits: List<ResponseSearch.Hit>) {}
        }
        val connection = searcherSingleIndex?.connectHitsView(hitsView) { it -> it.hits }
        connection?.let {
            it.connect()
            if (it.isConnected) it.disconnect()
        }
    }

    @Test
    fun loading() {
        val viewModel = LoadingViewModel()
        viewModel.eventReload
        viewModel.isLoading

        val view = object : LoadingView {
            override var onReload: Callback<Unit>? = null
            override fun setIsLoading(isLoading: Boolean) {}
        }
        viewModel.connectView(view)
        searcherSingleIndex?.let { viewModel.connectSearcher(it) }
        searcherSingleIndex?.let { viewModel.connectSearcher(it, Debouncer(42)) }
    }

    @Test
    fun map() {
        // ViewModel
        val viewModel = MapViewModel(mapOf("id" to "value"))

        viewModel.event.subscribe { viewModel.map.value = it }
        viewModel.remove("id")
        viewModel.map.value
    }

    @Test
    fun number() {
        // Computation
        var valueInt: Int? = null
        val computationInt: Computation<Int> = { valueInt = it(valueInt) }
        computationInt.increment(1, 1)
        computationInt.decrement(1)
        var valueLong: Long? = null
        val computationLong: Computation<Long> = { valueLong = it(valueLong) }
        computationLong.increment(1, 1)
        computationLong.decrement(1)
        var valueFloat: Float? = null
        val computationFloat: Computation<Float> = { valueFloat = it(valueFloat) }
        computationFloat.increment(1f, 1f)
        computationFloat.decrement(1f)
        var valueDouble: Double? = null
        val computationDouble: Computation<Double> = { valueDouble = it(valueDouble) }
        computationDouble.increment(1.0, 1.0)
        computationDouble.decrement(1.0)

        // ViewModel
        var viewModel = NumberViewModel<Int>()
        viewModel = NumberViewModel(0)
        viewModel = NumberViewModel(0, Range(0..10))
        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        viewModel.coerce(-1)
        viewModel.number.value
        viewModel.bounds.value = Range(0..10)
        viewModel.setBoundsFromFacetStatsInt(attribute, facetStats)
        // Presenter
        val presenter = NumberPresenterImpl
        presenter(10)

        // View
        val view = object : NumberView<Int> {
            override fun setText(text: String) {}
            override fun setComputation(computation: Computation<Int>) {}
        }
        viewModel.connectView(view)
        viewModel.connectView(view, presenter)
    }

    @Test
    fun number_range() {
        // Range
        val bounds = Range(0L, 100L)
        val range = Range(LongRange(10L, 20L))

        // ViewModel
        val viewModel = NumberRangeViewModel(range, bounds)
        viewModel.eventRange.subscribe { viewModel.range.value = it }
        viewModel.coerce(bounds)
        viewModel.range.value
        viewModel.connectFilterState(filterState, attribute)
        viewModel.connectFilterState(filterState, attribute, FilterGroupID(FilterOperator.Or))

        val view = object : NumberRangeView<Long> {
            override var onRangeChanged: Callback<Range<Long>>? = null
            override fun setRange(range: Range<Long>?) {}
            override fun setBounds(bounds: Range<Long>?) {}

        }
        viewModel.connectView(view)
    }

    @Test
    fun searchbox() {
        val viewModel = SearchBoxViewModel()
        viewModel.query.subscribe { println(it) }
        viewModel.eventSubmit.send("foo")

        // TODO View - can't be done without a Java-friendly `Callback()` due to onQueryXX
        val view = object : SearchBoxView {
            override var onQueryChanged: Callback<String?>? = null
            override var onQuerySubmitted: Callback<String?>? = null
            override fun setText(text: String?, submitQuery: Boolean) {}
        }
        viewModel.connectView(view)
    }

    @Test
    fun searcher() {
        // Constants
        val constA = debounceLoadingInMillis
        val constB = debounceSearchInMillis
        val constC = debounceFilteringInMillis

        // TODO Debouncer - currently blocked by creation of CoroutineScope, then needs a refactoring around Function1/Function2

        // Searcher
        searchers.forEach { searcher ->
            searcher?.error?.subscribe { println(it) }
            searcher?.response
            searcher?.isLoading?.subscribe { println(if (it) "Loading..." else "Done loading") }
            searcher?.setQuery("foo")
            searcher?.searchAsync()
            searcher?.cancel()
        }

        // SearcherSingleIndex
        var index = searcherSingleIndex?.index
        var query = searcherSingleIndex?.query
        var requestOptions = searcherSingleIndex?.requestOptions
        val isDisjunctiveFacetingEnabled = searcherSingleIndex?.isDisjunctiveFacetingEnabled
        val responseSearch = searcherSingleIndex?.response?.value

        // SearcherMultipleIndex
        val queries = searcherMultipleIndex?.queries
        val client = searcherMultipleIndex?.client
        requestOptions = searcherMultipleIndex?.requestOptions
        val strategy = searcherMultipleIndex?.strategy
        val responseSearches = searcherMultipleIndex?.response?.value

        // SearcherForFacets
        index = searcherForFacets?.index
        val attribute = searcherForFacets?.attribute
        query = searcherForFacets?.query
        val facetQuery = searcherForFacets?.facetQuery
        requestOptions = searcherForFacets?.requestOptions
        val responseSearchForFacets = searcherForFacets?.response?.value

        // Sequencer
        val sequencer = Sequencer()
        val maxOperations = sequencer.maxOperations
        searchers.forEach { it?.let { sequencer.addOperation(it.searchAsync()) } }
        sequencer.currentOperationOrNull?.cancel()
        sequencer.cancelAll()
    }

    @Test
    fun selectable() {
        val viewModel = SelectableItemViewModel("foo")
        viewModel.eventSelection.subscribe { println(if (it) "Selected!" else "No more") }
        viewModel.isSelected.value
        viewModel.item.value = "bar"

        val view = object : SelectableItemView<String> {
            override var onSelectionChanged: Callback<Boolean>? = null
            override fun setItem(item: String) {}
            override fun setIsSelected(isSelected: Boolean) {}
        }
        viewModel.connectView(view) { it }
    }

    @Test
    fun selectable_list() {
        val item = SelectableItem("foo", false)
        item.first
        item.second

        val viewModel =
            SelectableListViewModel<String, String>(selectionMode = SelectionMode.Multiple)
        viewModel.items.value
        viewModel.selectionMode.name
        viewModel.selections.subscribe { println("New selections: $it") }
        viewModel.eventSelection.subscribe { println("Selected $it") }

        val view = object : SelectableListView<String> {
            override var onSelection: Callback<String>? = null
            override fun setItems(items: List<SelectableItem<String>>) {}
        }
        viewModel.connectView(view)
    }

    @Test
    fun selectable_map() {
        val viewModel = SelectableMapViewModel<String, String>()
        viewModel.event.subscribe { println(it) }
        viewModel.eventSelection.subscribe { println("New selection: $it") }
        viewModel.selected.value = "foo"
        viewModel.map.value

        val view = object : SelectableMapView<String, String> {
            override var onSelectionChange: Callback<String>? = null

            override fun setMap(map: Map<String, String>) {}

            override fun setSelected(selected: String?) {}

        }
        viewModel.connectView(view) { it }
    }

    @Test
    fun subscription() {
        val subscription = Subscription<Boolean>()
        val block: (Boolean) -> Unit = {
            val foo = !it
        }
        subscription.subscribe(block)
        subscription.unsubscribe(block)
        subscription.unsubscribeAll()
    }

    @Test
    fun tree() {
        val node = Node("Foo")
        val tree = Tree(mutableListOf(node))
        tree.children.size

        val viewModel: TreeViewModel<String, String> = object : TreeViewModel<String, String>() {
            override fun computeSelections(key: String) {}
        }

        val view = object : TreeView<String, Tree<String>> {
            override var onSelectionChanged: Callback<String>? = null
            override fun setTree(tree: Tree<String>) {}
        }
        viewModel.connectView(view) { it }
    }

    @Test
    fun presenter() {

    }
    //endregion

    //region Helper.commonMain
    @Test
    fun attribute() {
        val matchAndReplace = AttributeMatchAndReplace(Attribute("foo"), "bar")
        val toReplace = Attribute("the fool!")
        matchAndReplace(toReplace)

        val presenter = AttributePresenterImpl()
        presenter(toReplace)
    }

    @Test
    fun filter() {
        val presenter: FilterPresenter = FilterPresenterImpl()
        presenter(Filter.Facet(attribute, "foo"))
    }

    @Test
    fun filter_clear() {
        val viewModel = FilterClearViewModel()
        viewModel.eventClear.send()

        viewModel.connectFilterState(filterState)
        viewModel.connectFilterState(filterState, groupIDs)
        viewModel.connectFilterState(filterState, groupIDs, ClearMode.Except)

        val view = object : FilterClearView {
            override var onClear: Callback<Unit>? = null
        }
        viewModel.connectView(view)
    }

    @Test
    fun filter_current() {
        // ViewModel
        val viewModel = FilterCurrentViewModel()
        viewModel.connectFilterState(filterState)
        viewModel.connectFilterState(filterState, groupIDs)

        val view = object : FilterCurrentView {
            override var onFilterSelected: Callback<FilterAndID>? = null
            override fun setFilters(filters: List<Pair<FilterAndID, String>>) {}
        }
        viewModel.connectView(view)

        // Presenter
        val presenter = FilterCurrentPresenterImpl()
        val filterMap = mapOf(FilterAndID(FilterGroupID(), filterFacet) to filterFacet)
        presenter.present(filterMap)
    }

    @Test
    fun filter_facet() {
        val facetPairs = listOf(
            Facet("a", 1, null) to false,
            Facet("b", 2, null) to false
        )

        // ViewModel
        var viewModel = FacetListViewModel()
        viewModel = FacetListViewModel(facets)
        viewModel = FacetListViewModel(facets, SelectionMode.Multiple)
        viewModel = FacetListViewModel(facets, SelectionMode.Multiple, true)
        viewModel.facets.value = facetPairs
        val persistentSelection = viewModel.persistentSelection
        val selectionMode = viewModel.selectionMode

        //TODO: Uncomment once overload resolution ambiguity resolved
//        viewModel.connectFilterState(filterState, attribute)
        viewModel.connectFilterState(filterState, attribute, groupIDs[0])
        viewModel.connectFilterState(filterState, attribute, FilterOperator.Or)

        val view = object : FacetListView {
            override var onSelection: Callback<Facet>? = null
            override fun setItems(items: List<SelectableItem<Facet>>) {}
        }
        viewModel.connectView(view)

        // Presenter
        //FIXME: Why does IDE say `cannot be applied to`?
        FacetListPresenterImpl().present(facetPairs)
    }


    @Test
    fun filter_list() {
        // ViewModel
        val viewModelFacet = FilterListViewModel.Facet()
        var filters: List<Filter> = viewModelFacet.items.value
        val viewModelAll = FilterListViewModel.All()
        filters = viewModelAll.items.value

        val view = object : FilterListView.All {
            override var onSelection: Callback<Filter>? = null

            override fun setItems(items: List<SelectableItem<Filter>>) {}

        }
        viewModelAll.connectView(view)
    }

    @Test
    fun filter_map() {
        // ViewModel
        val viewModel = FilterMapViewModel()

        val view = object : SelectableMapView<Int, Filter> {
            override var onSelectionChange: Callback<Int>? = null

            override fun setMap(map: Map<Int, Filter>) {}

            override fun setSelected(selected: Int?) {}
        }
        viewModel.connectView(view) { it }
    }


    @org.junit.Test
    fun filter_comparison() {
        // ViewModel
        val viewModel = NumberViewModel<Int>()
        viewModel.connectFilterState(filterState, attribute, NumericOperator.Equals)
        viewModel.connectFilterState(filterState, attribute, NumericOperator.Equals, groupIDs[0])
    }

    @Test
    fun filter_state() {
        var groupID = FilterGroupID(FilterOperator.Or)
        groupID = FilterGroupID("groupID")
        groupID = FilterGroupID(attribute, FilterOperator.And)
        val filters = filterState.filters.value
    }


    @Test
    fun filter_toggle() {
        // ViewModel
        val viewModel = SelectableItemViewModel<Filter>(filterFacet)
        viewModel.connectFilterState(filterState)
        viewModel.connectFilterState(filterState, groupIDs[0])
    }

    @Test
    fun hierarchical() {
        val hierarchicalAttributes = listOf(attribute)

        // Item
        val (facet, displayName, level) = HierarchicalItem(facet, "item", 0)

        // Node
        val (content, children) = Node(facet)

        // Filter
        val (attributes1, path1, filter1) = HierarchicalFilter(
            hierarchicalAttributes,
            listOf(filterFacet), filterFacet
        )
        val attributes = attributes1
        val (attribute1, isNegated, value, score) = filter1
        val path = path1

        // Tree
        val tree = Tree(mutableListOf(Node(facet)))

        // ViewModel
        val viewModelMin = HierarchicalViewModel(
            attribute,
            hierarchicalAttributes, " > "
        )
        val viewModel = HierarchicalViewModel(attribute, hierarchicalAttributes, " > ", tree)
        val selections = viewModel.selections.value
        val hierarchicalAttributes1 = viewModel.hierarchicalAttributes
        val attribute = viewModel.attribute
        viewModel.eventHierarchicalPath.subscribe { it ->
            // Put in a never running handler to avoid calling addFacet on mocked Searcher
            viewModel.connectFilterState(filterState)
            searcherSingleIndex?.let {viewModel.connectSearcher(it) }
        }

        val presenter = HierarchicalPresenterImpl(" > ")
        presenter(tree)
    }
    //endregion

    //region Helper.androidMain
    //endregion

    companion object {
        @AfterClass
        @JvmStatic
        fun tearDown() {
            println("Kotlin DX Rocks! \uD83D\uDE80")
        }
    }
}
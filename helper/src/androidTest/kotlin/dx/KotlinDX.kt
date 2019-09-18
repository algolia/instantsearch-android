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
import com.algolia.instantsearch.core.tree.*
import com.algolia.instantsearch.helper.attribute.AttributeMatchAndReplace
import com.algolia.instantsearch.helper.attribute.AttributePresenterImpl
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.loading.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
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
    private var attribute = Attribute("foo")

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

        val view = object : NumberRangeView<Long> {
            override var onRangeChanged: Callback<Range<Long>>? = null
            override fun setRange(range: Range<Long>?) {}
            override fun setBounds(bounds: Range<Long>?) {}

        }
        viewModel.connectView(view)
        viewModel.connectFilterState(filterState, attribute)
        viewModel.connectFilterState(filterState, attribute, FilterGroupID(FilterOperator.Or))
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
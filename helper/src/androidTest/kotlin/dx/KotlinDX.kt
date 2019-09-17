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
import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.NumberPresenterImpl
import com.algolia.instantsearch.core.number.decrement
import com.algolia.instantsearch.core.number.increment
import com.algolia.instantsearch.core.searcher.*
import com.algolia.instantsearch.core.subscription.Subscription
import com.algolia.instantsearch.helper.loading.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.response.ResponseSearch
import org.junit.AfterClass
import kotlin.test.Test

internal class KotlinDX {

    //TODO: Maybe it's worth introducing mocked objects to avoid cumbersome nullability
    private var searcherSingleIndex: SearcherSingleIndex? = null
    private var searcherMultipleIndex: SearcherMultipleIndex? = null
    private var searcherForFacets: SearcherForFacets? = null
    private var searchers: List<Searcher<out Any>?> =
        listOf(searcherSingleIndex, searcherMultipleIndex, searcherForFacets)

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
            private var hits: List<ResponseSearch.Hit>? = null

            override fun setHits(hits: List<ResponseSearch.Hit>) {
                this.hits = hits
            }
        }
        val connection =
            searcherSingleIndex?.connectHitsView(hitsView) { it: ResponseSearch -> it.hits }
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
            private var isLoading: Boolean = false

            override var onReload: Callback<Unit>? = null

            override fun setIsLoading(isLoading: Boolean) {
                this.isLoading = isLoading
            }
        }
        viewModel.connectView(view)
        searcherSingleIndex?.let { viewModel.connectSearcher(it) }
        searcherSingleIndex?.let { viewModel.connectSearcher(it, Debouncer(42)) }
    }

    @Test
    fun map() {
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
        val viewModel = MapViewModel(mapOf("id" to "value"))
        viewModel.event.subscribe { viewModel.map.value = it }
        viewModel.remove("id")
        viewModel.map.value

        // Presenter
        NumberPresenterImpl(10)
    }

    @Test
    fun number() {
        // TODO
    }

    @Test
    fun searchbox() {
        // TODO
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
        sequencer.cancelAll(); }

    @Test
    fun searchable() {
        // TODO
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
        // TODO
    }

    companion object {
        @AfterClass
        @JvmStatic
        fun tearDown() {
            println("Kotlin DX Rocks! \uD83D\uDE80")
        }
    }
}
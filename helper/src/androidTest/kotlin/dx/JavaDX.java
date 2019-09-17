package dx;

import com.algolia.instantsearch.core.connection.Connection;
import com.algolia.instantsearch.core.connection.ConnectionHandler;
import com.algolia.instantsearch.core.connection.ConnectionImpl;
import com.algolia.instantsearch.core.highlighting.HighlightTags;
import com.algolia.instantsearch.core.highlighting.HighlightToken;
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer;
import com.algolia.instantsearch.core.highlighting.HighlightedString;
import com.algolia.instantsearch.core.hits.Hits;
import com.algolia.instantsearch.core.hits.HitsView;
import com.algolia.instantsearch.core.loading.LoadingViewModel;
import com.algolia.instantsearch.core.map.MapViewModel;
import com.algolia.instantsearch.core.number.NumberPresenterImpl;
import com.algolia.instantsearch.core.number.NumberViewModel;
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel;
import com.algolia.instantsearch.core.number.range.Range;
import com.algolia.instantsearch.core.searcher.Debouncer;
import com.algolia.instantsearch.core.searcher.Searcher;
import com.algolia.instantsearch.core.searcher.SearcherConstants;
import com.algolia.instantsearch.core.searcher.Sequencer;
import com.algolia.instantsearch.core.subscription.Subscription;
import com.algolia.instantsearch.core.subscription.SubscriptionValue;
import com.algolia.instantsearch.helper.loading.Loading;
import com.algolia.instantsearch.helper.searcher.SearcherForFacets;
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex;
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex;
import com.algolia.search.client.ClientSearch;
import com.algolia.search.client.Index;
import com.algolia.search.model.Attribute;
import com.algolia.search.model.multipleindex.IndexQuery;
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy;
import com.algolia.search.model.response.ResponseSearch;
import com.algolia.search.model.response.ResponseSearchForFacets;
import com.algolia.search.model.response.ResponseSearches;
import com.algolia.search.model.search.Query;
import com.algolia.search.transport.RequestOptions;

import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CancellationException;

import kotlin.ranges.LongRange;
import kotlinx.coroutines.Job;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//FIXME: Why is mockito not visible from IDE?


@SuppressWarnings("UnusedAssignment")
public class JavaDX {

    private static SearcherSingleIndex searcherSingleIndex;
    private static SearcherMultipleIndex searcherMultipleIndex;
    private static SearcherForFacets searcherForFacets;
    private static List<Searcher<?>> searchers;

    private static Query query = mock(Query.class);
    private static ClientSearch client = mock(ClientSearch.class);
    private static RequestOptions requestOptions = new RequestOptions();

    //region Test Setup
    @BeforeClass
    public static void setUp() {
        // region Prepare Searcher mocks
        final SubscriptionValue<Throwable> error = new SubscriptionValue<>(new Exception());
        final SubscriptionValue<ResponseSearch> responseSearch = new SubscriptionValue<>(new ResponseSearch());
        final SubscriptionValue<ResponseSearches> responseSearches = new SubscriptionValue<>(new ResponseSearches(Collections.singletonList(responseSearch.getValue())));
        final SubscriptionValue<ResponseSearchForFacets> responseSearchForFacet = new SubscriptionValue<>(new ResponseSearchForFacets(Collections.emptyList(), true, 0));

        searcherSingleIndex = mock(SearcherSingleIndex.class);
        searcherMultipleIndex = mock(SearcherMultipleIndex.class);
        searcherForFacets = mock(SearcherForFacets.class);
        searchers = Arrays.asList(searcherSingleIndex, searcherMultipleIndex, searcherForFacets);

        searchers.forEach(searcher -> {
            when(searcher.getError()).thenReturn(error);
            when(searcher.isLoading()).thenReturn(new SubscriptionValue<>(true));
            when(searcher.searchAsync()).thenReturn(mock(Job.class));
        });
        //FIXME: Why does IDE say `Cannot resolve method`?
        when(searcherSingleIndex.getResponse()).thenReturn(responseSearch);
        when(searcherMultipleIndex.getResponse()).thenReturn(responseSearches);
        when(searcherForFacets.getResponse()).thenReturn(responseSearchForFacet);
        //endregion
    }


    @AfterClass
    public static void tearDown() {
        System.out.println("Java DX Rocks! \uD83D\uDC4C");
    }
    //endregion

    //region Core
    @Test
    public final void connection() {
        ConnectionHandler handler = new ConnectionHandler();
        handler.add(new ConnectionImpl());
        handler.disconnect();
    }

    @Test
    public final void highlighting() {
        // Token
        HighlightToken token = new HighlightToken("foo", true);
        final String content = token.content;
        final boolean highlighted = token.highlighted;

        // Tokenizer
        HighlightTokenizer tokenizer = new HighlightTokenizer();
        String preTag = HighlightTags.DefaultPreTag;
        String postTag = HighlightTags.DefaultPostTag;
        tokenizer = new HighlightTokenizer(preTag);
        tokenizer = new HighlightTokenizer(preTag, postTag);
        final String preTagGot = tokenizer.preTag;
        final String postTagGot = tokenizer.postTag;

        // String
        final HighlightedString invoke = tokenizer.tokenize("foo<b>bar</b>");
        final List<HighlightToken> tokens = invoke.tokens;
        invoke.getHighlightedTokens();
        final String original = invoke.original;
    }

    @Test
    public void hits() {
        HitsView<ResponseSearch.Hit> hitsView = new HitsView<ResponseSearch.Hit>() {
            private List<? extends ResponseSearch.Hit> hits;

            @Override
            public void setHits(@NotNull List<? extends ResponseSearch.Hit> hits) {
                this.hits = hits;
            }
        };
        final Connection connection = Hits.<ResponseSearch, ResponseSearch.Hit>connectHitsView(searcherSingleIndex,
                hitsView,
                it -> it.getHits() // FIXME: Why doesn't IDE see the getHits method?
        );
        connection.connect();
        if (connection.isConnected()) connection.disconnect();
    }

    @Test
    public void loading() {
        // ViewModel
        LoadingViewModel viewModel = new LoadingViewModel();
        viewModel.eventReload.subscribe(it -> {
        });
        viewModel.isLoading.getValue();

        // TODO LoadingView - can't be done without a Java-friendly `Callback()` due to onReload
//        LoadingView view = new LoadingView() {
//            @Nullable
//            @Override
//            public Function1<Unit, Unit> getOnReload() {
//                return null;
//            }
//
//            @Override
//            public void setOnReload(@Nullable Function1<? super Unit, Unit> nonExistentClass) {
//
//            }
//
//            @Override
//            public void setIsLoading(boolean isLoading) {
//
//            }
//        }
//        viewModel.connectView(view)
        //FIXME: Why does IDE report "Wrong 2nd argument type"?
        Loading.connectSearcher(viewModel, searcherSingleIndex);
        Loading.connectSearcher(viewModel, searcherSingleIndex, new Debouncer(42));
    }

    @Test
    public void map() {
        // ViewModel
        final HashMap<String, String> initialMap = new HashMap<>();
        initialMap.put("id", "value");
        MapViewModel<String, String> viewModel = new MapViewModel<>(initialMap);

        viewModel.event.subscribe(viewModel.map::setValue);
        viewModel.remove("id");
        viewModel.map.getValue();
    }

    @Test
    public void number() {
        Integer valueInt = 0;
//        Computation computation = it -> valueInt = it(valueInt);
//        computation.increment(1, 1);
//        computation.decrement(1);
        //TODO: Refactor Computation as SMI to allow use from Java

        // ViewModel
        NumberViewModel<Integer> viewModel = new NumberViewModel<>();
        viewModel.eventNumber.subscribe(viewModel.number::setValue);
        viewModel.coerce(-1);
        viewModel.number.getValue();
        viewModel.bounds.setValue(new Range<>(0, 10));

        // Presenter
        NumberPresenterImpl.INSTANCE.present(10);

        // TODO View

        // TODO Connection
    }

    @Test
    public void number_range() {
        Range<Long> bounds = new Range<>(0L, 100L);
        // FIXME: Why does this display as error but compiles fine?
        Range<Long> range = Range.Companion.invoke(new LongRange(10L, 20L));

        // ViewModel
        NumberRangeViewModel<Long> viewModel = new NumberRangeViewModel<>(range, bounds);
        viewModel.eventRange.subscribe(viewModel.range::setValue);
        viewModel.coerce(bounds);
        viewModel.range.getValue();

        // TODO View
        // TODO Connection
    }

    @Test
    public void searchbox() {
        // TODO
    }

    @Test
    public void searcher() {
        // Constants
        long constA = SearcherConstants.debounceLoadingInMillis;
        long constB = SearcherConstants.debounceSearchInMillis;
        long constC = SearcherConstants.debounceFilteringInMillis;

        // TODO Debouncer - currently blocked by creation of CoroutineScope, then needs a refactoring around Function1/Function2

        // Searcher
        searchers.forEach(searcher -> {
            searcher.getError().subscribe(System.out::println);
            searcher.getResponse().getValue();
            searcher.isLoading().subscribe(it -> System.out.println(it ? "Loading..." : "Done loading"));
            searcher.setQuery("foo");
            searcher.searchAsync();
            searcher.cancel();
        });

        // SearcherSingleIndex
        //FIXME: Why does IDE report `Incompatible types`?
        Index index = searcherSingleIndex.index;
        Query query = searcherSingleIndex.query;
        RequestOptions requestOptions = searcherSingleIndex.requestOptions;
        final Boolean isDisjunctiveFacetingEnabled = searcherSingleIndex.isDisjunctiveFacetingEnabled;
        final ResponseSearch responseSearch = searcherSingleIndex.getResponse().getValue();

        //FIXME: Why does IDE report `Incompatible types`?
        final List<IndexQuery> queries = searcherMultipleIndex.queries;
        final ClientSearch client = searcherMultipleIndex.client;
        requestOptions = searcherMultipleIndex.requestOptions;
        final MultipleQueriesStrategy strategy = searcherMultipleIndex.strategy;
        final ResponseSearches responseSearches = searcherMultipleIndex.getResponse().getValue();

        // SearcherForFacets
        index = searcherForFacets.index;
        final Attribute attribute = searcherForFacets.attribute;
        query = searcherForFacets.query;
        final String facetQuery = searcherForFacets.facetQuery;
        requestOptions = searcherForFacets.requestOptions;
        final ResponseSearchForFacets responseSearchForFacets = searcherForFacets.getResponse().getValue();

        // Sequencer
        Sequencer sequencer = new Sequencer();
        final int maxOperations = sequencer.maxOperations;
        searchers.forEach(s -> sequencer.addOperation(s.searchAsync()));
        //FIXME: Why does IDE report no `cancel` method?
        sequencer.getCurrentOperation().cancel(new CancellationException());
        sequencer.cancelAll();
    }

    @Test
    public void selectable() {

    }

    @Test
    public void selectable_list() {

    }

    @Test
    public void selectable_map() {

    }


    @Test
    public void subscription() {
        Subscription<Boolean> subscription = new Subscription<>();
        final Subscription.Callback<Boolean> callback = it -> fail("Why not?");
        subscription.subscribe(callback);
        subscription.unsubscribe(callback);
        subscription.unsubscribeAll();
    }

    @Test
    public void tree() {
        // TODO
    }
    //endregion
}
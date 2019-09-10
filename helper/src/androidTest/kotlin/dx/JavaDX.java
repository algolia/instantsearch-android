package dx;

import com.algolia.instantsearch.core.connection.ConnectionHandler;
import com.algolia.instantsearch.core.connection.ConnectionImpl;
import com.algolia.instantsearch.core.highlighting.HighlightTags;
import com.algolia.instantsearch.core.highlighting.HighlightToken;
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer;
import com.algolia.instantsearch.core.highlighting.HighlightedString;
import com.algolia.instantsearch.core.loading.LoadingViewModel;
import com.algolia.instantsearch.core.map.MapViewModel;
import com.algolia.instantsearch.core.number.NumberPresenterImpl;
import com.algolia.instantsearch.core.number.NumberViewModel;
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel;
import com.algolia.instantsearch.core.number.range.Range;
import com.algolia.instantsearch.core.searcher.SearcherConstants;
import com.algolia.instantsearch.core.subscription.Subscription;
import com.algolia.instantsearch.core.subscription.SubscriptionValue;
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex;
import com.algolia.search.client.ClientSearch;
import com.algolia.search.client.Index;
import com.algolia.search.model.response.ResponseSearch;
import com.algolia.search.model.search.Query;
import com.algolia.search.transport.RequestOptions;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import kotlin.ranges.LongRange;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SuppressWarnings("UnusedAssignment")
public class JavaDX {

    private static SearcherSingleIndex searcherSingleIndex;
    private static Query query = mock(Query.class);
    private static ClientSearch client = mock(ClientSearch.class);
    private static RequestOptions requestOptions = new RequestOptions();

    //region Test Setup
    @BeforeClass
    public static void setUp() {
        query = new Query();
        requestOptions = new RequestOptions();


        final SubscriptionValue<Throwable> error = new SubscriptionValue<>(new Exception());
        final SubscriptionValue<ResponseSearch> responseSearch = new SubscriptionValue<>(new ResponseSearch());
        searcherSingleIndex = mock(SearcherSingleIndex.class);
        when(searcherSingleIndex.getError()).thenReturn(error);
        when(searcherSingleIndex.getResponse()).thenReturn(responseSearch);
        when(searcherSingleIndex.isLoading()).thenReturn(new SubscriptionValue<>(true));
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
        // TODO All
    }

    @Test
    public void loading() {
        // ViewModel
        LoadingViewModel viewModel = new LoadingViewModel();
        viewModel.eventReload.subscribe(it -> {
        });
        viewModel.isLoading.getValue();

        // TODO View
        // TODO Connection
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
        // TODO Computation - will need refactor typealias into interface

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

        // TODO Debouncer - currently blocked by creation of CoroutineScope, then  needs a refactoring around Function1/Function2

        // Searcher

        searcherSingleIndex.getError().subscribe(System.out::println);
        searcherSingleIndex.getResponse().getValue();
        searcherSingleIndex.isLoading().subscribe(it -> System.out.println(it ? "Loading..." : "Done loading"));
        searcherSingleIndex.setQuery("foo");
        searcherSingleIndex.searchAsync();
        searcherSingleIndex.cancel();

        // SearcherSingleIndex
        final Index index = searcherSingleIndex.index;
        final Query query = searcherSingleIndex.query;
        final RequestOptions requestOptions = searcherSingleIndex.requestOptions;
        final Boolean isDisjunctiveFacetingEnabled = searcherSingleIndex.isDisjunctiveFacetingEnabled;

        // TODO SearcherMultipleIndex
        // TODO SearcherForFacets

    }

    @Test
    public void searchable() {
        // TODO
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
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
import com.algolia.instantsearch.core.searcher.Searcher;
import com.algolia.instantsearch.core.searcher.SearcherConstants;
import com.algolia.instantsearch.core.subscription.Subscription;
import com.algolia.search.client.ClientSearch;
import com.algolia.search.client.Index;
import com.algolia.search.model.search.Query;
import com.algolia.search.transport.RequestOptions;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import kotlin.ranges.LongRange;

import static org.junit.Assert.fail;


@SuppressWarnings("UnusedAssignment")
public class JavaDX {
    private static Searcher searcher;
    private static Index index;
    private static Query query;
    private static ClientSearch client;
    private static RequestOptions requestOptions;

    //region Test Setup
    @BeforeClass
    public static void setUp() {
        query = new Query();
        requestOptions = new RequestOptions();
//        client = new ClientSearch(new ApplicationID("foo"), new APIKey("bar"), LogLevel.ALL);
//        index = client.initIndex(new IndexName("indexName"));
//        searcher = new SearcherSingleIndex(index, query, requestOptions);
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
        token.getContent();
        token.getHighlighted();

        // Tokenizer
        HighlightTokenizer tokenizer = new HighlightTokenizer();
        String preTag = HighlightTags.DefaultPreTag;
        String postTag = HighlightTags.DefaultPostTag;
        tokenizer = new HighlightTokenizer(preTag);
        tokenizer = new HighlightTokenizer(preTag, postTag);
        tokenizer.getPreTag();
        tokenizer.getPostTag();

        // String
        final HighlightedString invoke = tokenizer.tokenize("foo<b>bar</b>");
        invoke.getTokens();
        invoke.getHighlightedTokens();
        invoke.getOriginal();
    }

    @Test
    public void hits() {
        // TODO All
    }

    @Test
    public void loading() {
        // ViewModel
        LoadingViewModel viewModel = new LoadingViewModel();
        viewModel.getEventReload();
        viewModel.isLoading();

        // TODO View
        // TODO Connection
    }

    @Test
    public void map() {
        // ViewModel
        final HashMap<String, String> initialMap = new HashMap<>();
        initialMap.put("id", "value");
        MapViewModel<String, String> viewModel = new MapViewModel<>(initialMap);

        viewModel.getEvent().subscribe(item -> viewModel.getMap().setValue(item));
        viewModel.remove("id");
        viewModel.getMap().getValue();
    }

    @Test
    public void number() {
        // TODO Computation - will need refactor typealias into interface

        // ViewModel
        NumberViewModel<Integer> viewModel = new NumberViewModel<>();
        viewModel.getEventNumber().subscribe(item -> viewModel.getNumber().setValue(item));
        viewModel.coerce(-1);
        viewModel.getNumber().getValue();
        viewModel.getBounds().setValue(new Range<>(0, 10));

        // Presenter
        NumberPresenterImpl.INSTANCE.present(10);

        // TODO View

        // TODO Connection
    }

    @Test
    public void number_range() {
        Range<Long> bounds = new Range<>(0L, 100L);
        Range<Long> range = new Range<Long>(new LongRange(10L, 20L));

        // ViewModel
        NumberRangeViewModel<Long> viewModel = new NumberRangeViewModel<>(range, bounds);
        viewModel.getEventRange().subscribe(item -> viewModel.getRange().setValue(item));
        viewModel.coerce(bounds);
        viewModel.getRange().getValue();

        // TODO View
        // TODO Connection
    }

    @Test
    public void searchbox() {
        // TODO
    }

    @Test
    public void searcher() {
        long constA = SearcherConstants.debounceLoadingInMillis;
        long constB = SearcherConstants.debounceSearchInMillis;
        long constC = SearcherConstants.debounceFilteringInMillis;


    }

    @Test
    public void searchable() {
        // TODO
    }


    @Test
    public void subscription() {
        Subscription<Boolean> subscription = new Subscription<>();
        final Subscription.Callback<Boolean> callback = item -> fail("Why not?");
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
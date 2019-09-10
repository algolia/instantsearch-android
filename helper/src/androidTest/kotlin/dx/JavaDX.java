package dx;

import android.util.Log;

import com.algolia.instantsearch.core.connection.ConnectionHandler;
import com.algolia.instantsearch.core.connection.ConnectionImpl;
import com.algolia.instantsearch.core.highlighting.HighlightTags;
import com.algolia.instantsearch.core.highlighting.HighlightToken;
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer;
import com.algolia.instantsearch.core.highlighting.HighlightedString;
import com.algolia.instantsearch.core.loading.LoadingViewModel;
import com.algolia.instantsearch.core.searcher.Searcher;
import com.algolia.instantsearch.core.subscription.Subscription;
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex;
import com.algolia.search.client.ClientSearch;
import com.algolia.search.client.Index;
import com.algolia.search.model.APIKey;
import com.algolia.search.model.ApplicationID;
import com.algolia.search.model.IndexName;
import com.algolia.search.model.search.Query;
import com.algolia.search.transport.RequestOptions;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.ktor.client.features.logging.LogLevel;
import kotlin.Unit;

import static org.junit.Assert.fail;


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
        // TODO
    }

    @Test
    public void loading() {
        LoadingViewModel viewModel = new LoadingViewModel();
        viewModel.getEventReload();
        viewModel.isLoading();
    }

    @Test
    public void map() {
        // TODO
    }

    @Test
    public void number() {
        // TODO
    }

    @Test
    public void searchbox() {
        // TODO
    }

    @Test
    public void searcher() {
        // TODO
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
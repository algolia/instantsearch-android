package dx;

import com.algolia.instantsearch.core.connection.ConnectionHandler;
import com.algolia.instantsearch.core.connection.ConnectionImpl;
import com.algolia.instantsearch.core.highlighting.HighlightTags;
import com.algolia.instantsearch.core.highlighting.HighlightToken;
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer;
import com.algolia.instantsearch.core.highlighting.HighlightedString;

import org.junit.AfterClass;
import org.junit.Test;


public class JavaDX {

    @Test
    public final void testConnection() {
        ConnectionHandler handler = new ConnectionHandler();
        handler.add(new ConnectionImpl());
        handler.disconnect();
    }

    @Test
    public final void testHighlighting() {
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

    @AfterClass
    public static void tearDown() {
        System.out.println("Java DX Rocks! \uD83D\uDC4C");
    }
}
package dx

import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightToken
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import org.junit.AfterClass
import kotlin.test.Test

internal class KotlinDX {

    @Test
    fun testConnection() {
        val handler = ConnectionHandler()
        handler += ConnectionImpl()
        handler.disconnect()
    }

    @Test
    fun testHighlighting() {
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

    companion object {
        @AfterClass
        @JvmStatic
        fun tearDown() {
            println("Kotlin DX Rocks! \uD83D\uDE80")
        }
    }
}
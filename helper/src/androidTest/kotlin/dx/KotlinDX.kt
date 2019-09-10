package dx

import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.highlighting.DefaultPostTag
import com.algolia.instantsearch.core.highlighting.DefaultPreTag
import com.algolia.instantsearch.core.highlighting.HighlightToken
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.subscription.Subscription
import org.junit.AfterClass
import java.util.HashMap
import kotlin.test.Test

internal class KotlinDX {

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
        // TODO
    }

    @Test
    fun loading() {
        val viewModel = LoadingViewModel()
        viewModel.eventReload
        viewModel.isLoading
    }

    @Test
    fun map() {
        val viewModel = MapViewModel(mapOf("id" to "value"))

        viewModel.event.subscribe { viewModel.map.value = it }
        viewModel.remove("id")
        viewModel.map.value
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
        // TODO
    }

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
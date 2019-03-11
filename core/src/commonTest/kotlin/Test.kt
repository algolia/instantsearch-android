
import kotlinx.coroutines.CoroutineScope
import searcher.Searcher


internal expect fun blocking(block: suspend CoroutineScope.() -> Unit)

internal data class Facet(
    val attribute: String,
    val count: Int
)

internal val facets = listOf(
    Facet("Blue", 1),
    Facet("Red", 2),
    Facet("Green", 3)
)
internal val otherFacets = listOf(
    Facet("Blue", 1),
    Facet("Red", 2)
)

internal class MockSearcher : Searcher {

    var count = 0

    override fun search() {
        count++
    }

    override fun cancel() {
    }
}
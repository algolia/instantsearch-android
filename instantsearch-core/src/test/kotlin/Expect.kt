import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}

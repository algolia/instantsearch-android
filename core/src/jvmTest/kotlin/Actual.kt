import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


internal actual fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}
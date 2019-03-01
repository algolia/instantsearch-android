import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}
import kotlinx.coroutines.CoroutineScope

expect fun blocking(block: suspend CoroutineScope.() -> Unit)
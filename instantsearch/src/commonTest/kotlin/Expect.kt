import kotlinx.coroutines.CoroutineScope


internal expect fun blocking(block: suspend CoroutineScope.() -> Unit)
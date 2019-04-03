import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default

internal actual val mainDispatcher: CoroutineDispatcher
    get() = Default
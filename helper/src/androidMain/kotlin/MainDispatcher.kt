import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

internal actual val mainDispatcher: CoroutineDispatcher
    get() = Main

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass
import kotlin.test.*


internal expect fun blocking(block: suspend CoroutineScope.() -> Unit)
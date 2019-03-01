import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


internal actual val apiKey = APIKey(System.getenv("KOTLIN_CLIENT_API_KEY"))
internal actual val applicationID = ApplicationID(System.getenv("KOTLIN_CLIENT_APP_ID"))

internal actual fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


actual val apiKey = APIKey(System.getenv("ALGOLIA_KOTLIN_CLIENT_API_KEY")!!)
actual val applicationID = ApplicationID(System.getenv("ALGOLIA_KOTLIN_CLIENT_APP_ID")!!)

actual fun blocking(block: suspend CoroutineScope.() -> Unit) {
    runBlocking(block = block)
}
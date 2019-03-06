import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import kotlinx.coroutines.CoroutineScope


internal expect val apiKey : APIKey
internal expect val applicationID : ApplicationID

internal expect fun blocking(block: suspend CoroutineScope.() -> Unit)

internal val algolia get() = ClientSearch(applicationID, apiKey)
internal val index get() = algolia.initIndex(IndexName("products_android_demo"))
internal val index2 get() = algolia.initIndex(IndexName("products_android_demo_name_asc"))
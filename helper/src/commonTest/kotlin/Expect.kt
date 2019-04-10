import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel
import kotlinx.coroutines.CoroutineScope


internal expect val apiKey : APIKey
internal expect val applicationID : ApplicationID

internal expect fun blocking(block: suspend CoroutineScope.() -> Unit)

internal val algolia get() = ClientSearch(ConfigurationSearch(applicationID, apiKey, logLevel = LogLevel.ALL))
internal val indexA get() = algolia.initIndex(IndexName("products_android_demo"))
internal val indexB get() = algolia.initIndex(IndexName("products_android_demo_name_asc"))
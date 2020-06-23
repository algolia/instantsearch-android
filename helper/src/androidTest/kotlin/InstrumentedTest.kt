import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.algolia.instantsearch.helper.android.R
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.register


val applicationContext: Application
    get() = ApplicationProvider.getApplicationContext<Application>().apply {
        setTheme(R.style.Theme_MaterialComponents)
    }

val appID = "test app id"
val apiKey = "test api key"
val indexName1 = "test index name"
val indexName2 = "test index name 2"
val insights = Insights.register(applicationContext, appID, apiKey, indexName1)

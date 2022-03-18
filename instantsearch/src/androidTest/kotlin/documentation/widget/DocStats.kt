package documentation.widget

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.item.StatsTextView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.stats.StatsViewModel
import com.algolia.instantsearch.stats.connectSearcher
import com.algolia.instantsearch.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Ignore

@Ignore
internal class DocStats {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val searcher = HitsSearcher(client, IndexName("YourIndexName"))
        val statsViewModel = StatsViewModel()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val textView = TextView(this)
            val statsView = StatsTextView(textView)

            connection += statsViewModel.connectSearcher(searcher)
            connection += statsViewModel.connectView(statsView)

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            connection.disconnect()
            searcher.cancel()
        }
    }
}

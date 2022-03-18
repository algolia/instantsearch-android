package documentation.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.filter.current.FilterCurrentViewImpl
import com.algolia.instantsearch.filter.current.FilterCurrentView
import com.algolia.instantsearch.filter.current.FilterCurrentViewModel
import com.algolia.instantsearch.filter.current.connectFilterState
import com.algolia.instantsearch.filter.current.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.google.android.material.chip.ChipGroup
import org.junit.Ignore

@Ignore
internal class DocCurrentFilters {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val searcher = HitsSearcher(client, IndexName("YourIndexName"))
        val filterState = FilterState()
        val viewModel = FilterCurrentViewModel()
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val chipGroup = ChipGroup(this)
            val view: FilterCurrentView =
                FilterCurrentViewImpl(chipGroup)

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectFilterState(filterState)
            connection += viewModel.connectView(view)

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            connection.disconnect()
            searcher.cancel()
        }
    }
}

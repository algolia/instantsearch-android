package documentation.widget

import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.android.filter.toggle.FilterToggleViewCompoundButton
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.toggle.FilterToggleView
import com.algolia.instantsearch.filter.toggle.FilterToggleViewModel
import com.algolia.instantsearch.filter.toggle.connectFilterState
import com.algolia.instantsearch.filter.toggle.connectView
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import org.junit.Ignore

@Ignore
internal class DocFilterToggle {

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val searcher = HitsSearcher(client, IndexName("YourIndexName"))
        val filterState = FilterState()
        val attribute = Attribute("facetName")
        val filter = Filter.Facet(attribute, "facetValue")
        val viewModel = FilterToggleViewModel(filter)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val checkBox = CheckBox(this)
            val view: FilterToggleView =
                FilterToggleViewCompoundButton(checkBox)

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

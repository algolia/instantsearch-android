package documentation.widget

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.map.FilterMapView
import com.algolia.instantsearch.filter.map.FilterMapViewModel
import com.algolia.instantsearch.filter.map.connectFilterState
import com.algolia.instantsearch.filter.map.connectView
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.instantsearch.filter.Filter
import org.junit.Ignore

@Ignore
internal class DocFilterMap {

    class FilterMapViewRadioGroup(
        val radioGroup: RadioGroup,
    ) : FilterMapView,
        RadioGroup.OnCheckedChangeListener {

        override var onSelectionChange: Callback<Int>? = null

        init {
            radioGroup.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            onSelectionChange?.invoke(checkedId)
        }

        override fun setSelected(selected: Int?) {
            radioGroup.setOnCheckedChangeListener(null)
            radioGroup.check(selected ?: View.NO_ID)
            radioGroup.setOnCheckedChangeListener(this)
        }

        override fun setMap(map: Map<Int, String>) {
            for (index in 0 until radioGroup.childCount) {
                val view = radioGroup.getChildAt(index) as? RadioButton

                map[view?.id]?.let { view?.text = it }
            }
        }
    }

    class MyActivity : AppCompatActivity() {

        val gender = "gender"
        val filterState = FilterState()
        val client = ClientSearch(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")
        val filters = mapOf(
            0 to Filter.Facet(gender, "male"),
            1 to Filter.Facet(gender, "female")
        )
        val viewModel = FilterMapViewModel(filters)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val radioGroup = RadioGroup(this)
            val view = FilterMapViewRadioGroup(radioGroup)

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectFilterState(filterState)
            connection += viewModel.connectView(view)

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            searcher.cancel()
            connection.disconnect()
        }
    }
}

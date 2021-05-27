package documentation.widget

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.connectView
import com.algolia.instantsearch.core.number.decrement
import com.algolia.instantsearch.core.number.increment
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.NumericOperator
import org.junit.Ignore

@Ignore
class DocFilterComparison {

    class FilterPriceView(
        val textView: TextView,
        val arrowUp: ImageView,
        val arrowDown: ImageView,
        val price: Attribute,
        val operator: NumericOperator,
    ) : NumberView<Int> {

        init {
            textView.text = "$price ${operator.raw}"
        }

        override fun setComputation(computation: Computation<Int>) {
            arrowUp.setOnClickListener {
                computation.increment()
            }
            arrowDown.setOnClickListener {
                computation.decrement()
            }
        }

        override fun setText(text: String) {
            textView.text = text
        }
    }

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)
        val filterState = FilterState()
        val price = Attribute("price")
        val operator = NumericOperator.Greater
        val viewModel = NumberViewModel(range = 0..10)
        val connection = ConnectionHandler()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val textView = TextView(this)
            val arrowUp = ImageView(this)
            val arrowDown = ImageView(this)
            val view: NumberView<Int> = FilterPriceView(textView, arrowUp, arrowDown, price, operator)

            searcher.query.addFacet(price)

            connection += searcher.connectFilterState(filterState)
            connection += viewModel.connectView(view)
            connection += viewModel.connectFilterState(filterState, price, operator)

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            connection.disconnect()
            searcher.cancel()
        }
    }
}

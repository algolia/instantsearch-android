package smartsort

import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortPriorityView
import com.algolia.instantsearch.core.smartsort.SmartSortView
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.core.smartsort.connectView
import kotlin.test.Test
import shouldEqual

class TestSmartSortConnectView {

    @Test
    fun testSmartSortView() {
        val viewModel = SmartSortViewModel(SmartSortPriority.HitsCount)
        val view = object : SmartSortView<String> {
            var textView: String = RELEVANT
            override var didToggle: (() -> Unit)? = null
            override fun updateView(input: String) {
                textView = input
            }
        }

        val connection = viewModel.connectView(view) { priority ->
            when (priority) {
                SmartSortPriority.Relevancy -> RELEVANT
                else -> ALL
            }
        }
        connection.connect()

        view.didToggle?.invoke()

        viewModel.priority.value shouldEqual SmartSortPriority.Relevancy
        view.textView shouldEqual RELEVANT
    }

    @Test
    fun testSmartSortPriorityView() {
        val viewModel = SmartSortViewModel(SmartSortPriority.HitsCount)
        val view = object : SmartSortPriorityView {
            var state: SmartSortPriority? = null
            override var didToggle: (() -> Unit)? = null
            override fun updateView(input: SmartSortPriority?) {
                this.state = input
            }
        }

        val connection = viewModel.connectView(view)
        connection.connect()

        view.didToggle?.invoke()

        viewModel.priority.value shouldEqual SmartSortPriority.Relevancy
        view.state shouldEqual SmartSortPriority.Relevancy
    }

    companion object {
        private const val RELEVANT = "We removed some search results to show you the most relevant ones."
        private const val ALL = "Currently showing all results."
    }
}

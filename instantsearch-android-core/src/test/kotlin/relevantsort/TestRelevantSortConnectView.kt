package relevantsort

import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortPriorityView
import com.algolia.instantsearch.core.relevantsort.RelevantSortView
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import com.algolia.instantsearch.core.relevantsort.connectView
import kotlin.test.Test
import shouldEqual

class TestRelevantSortConnectView {

    @Test
    fun testRelevantSortView() {
        val viewModel = RelevantSortViewModel(RelevantSortPriority.HitsCount)
        val view = object : RelevantSortView<String> {
            var textView: String = RELEVANT
            override var didToggle: (() -> Unit)? = null
            override fun updateView(input: String) {
                textView = input
            }
        }

        val connection = viewModel.connectView(view) { priority ->
            when (priority) {
                RelevantSortPriority.Relevancy -> RELEVANT
                else -> ALL
            }
        }
        connection.connect()

        view.didToggle?.invoke()

        viewModel.priority.value shouldEqual RelevantSortPriority.Relevancy
        view.textView shouldEqual RELEVANT
    }

    @Test
    fun testRelevantSortPriorityView() {
        val viewModel = RelevantSortViewModel(RelevantSortPriority.HitsCount)
        val view = object : RelevantSortPriorityView {
            var state: RelevantSortPriority? = null
            override var didToggle: (() -> Unit)? = null
            override fun updateView(input: RelevantSortPriority?) {
                this.state = input
            }
        }

        val connection = viewModel.connectView(view)
        connection.connect()

        view.didToggle?.invoke()

        viewModel.priority.value shouldEqual RelevantSortPriority.Relevancy
        view.state shouldEqual RelevantSortPriority.Relevancy
    }

    companion object {
        private const val RELEVANT = "We removed some search results to show you the most relevant ones."
        private const val ALL = "Currently showing all results."
    }
}

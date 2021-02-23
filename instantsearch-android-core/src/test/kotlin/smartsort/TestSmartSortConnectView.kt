package smartsort

import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortView
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import com.algolia.instantsearch.core.smartsort.connectView
import kotlin.test.Test
import shouldEqual

class TestSmartSortConnectView {

    @Test
    fun test() {
        val viewModel = SmartSortViewModel(SmartSortPriority.HitsCount)
        val view = object : SmartSortView {
            var priority: SmartSortPriority? = null
            override var didToggle: (() -> Unit)? = null
            override fun priority(priority: SmartSortPriority?) {
                this.priority = priority
            }
        }
        viewModel.connectView(view).connect()

        view.didToggle?.invoke()

        viewModel.priority.value shouldEqual SmartSortPriority.Relevancy
        view.priority shouldEqual SmartSortPriority.Relevancy
    }
}

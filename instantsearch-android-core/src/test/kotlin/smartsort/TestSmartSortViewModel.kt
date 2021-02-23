package smartsort

import com.algolia.instantsearch.core.smartsort.SmartSortPriority
import com.algolia.instantsearch.core.smartsort.SmartSortViewModel
import org.junit.Test
import shouldEqual

class TestSmartSortViewModel {

    @Test
    fun testToggle() {
        val viewModel = SmartSortViewModel(SmartSortPriority.HitsCount)
        viewModel.toggle()
        viewModel.priority.value shouldEqual SmartSortPriority.Relevancy
        viewModel.toggle()
        viewModel.priority.value shouldEqual SmartSortPriority.HitsCount
    }
}

package relevantsort

import com.algolia.instantsearch.core.relevantsort.RelevantSortPriority
import com.algolia.instantsearch.core.relevantsort.RelevantSortViewModel
import org.junit.Test
import shouldEqual

class TestRelevantSortViewModel {

    @Test
    fun testToggle() {
        val viewModel = RelevantSortViewModel(RelevantSortPriority.HitsCount)
        viewModel.toggle()
        viewModel.priority.value shouldEqual RelevantSortPriority.Relevancy
        viewModel.toggle()
        viewModel.priority.value shouldEqual RelevantSortPriority.HitsCount
    }
}

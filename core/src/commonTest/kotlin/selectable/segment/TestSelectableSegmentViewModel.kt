package selectable.segment

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentViewModel
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestSelectableSegmentViewModel {

    private val items = mapOf(0 to "A", 1 to "B")

    @Test
    fun noSelectedByDefault() {
        SelectableSegmentViewModel(items).apply {
            selected.value.shouldBeNull()
        }
    }

    @Test
    fun computeSelected() {
        SelectableSegmentViewModel(items).apply {
            eventSelection.subscribe { selected.value = it }
            eventSelection.send(1)
            selected.value shouldEqual 1
        }
    }
}
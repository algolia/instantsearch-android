package selectable.segment

import com.algolia.instantsearch.core.selectable.segment.SelectableSegmentViewModel
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestSelectableSegmentViewModel  {

    private val items = mapOf(0 to "A", 1 to "B")

    @Test
    fun noSelectedByDefault() {
        SelectableSegmentViewModel(items).apply {
            selected.shouldBeNull()
        }
    }

    @Test
    fun computeSelected() {
        SelectableSegmentViewModel(items).apply {
            onSelectedComputed += { selected = it }
            computeSelected(1)
            selected shouldEqual 1
        }
    }
}
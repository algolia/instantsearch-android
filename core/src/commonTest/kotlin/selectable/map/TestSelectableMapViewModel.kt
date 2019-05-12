package selectable.map

import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestSelectableMapViewModel  {

    private val items = mapOf(0 to "A", 1 to "B")

    @Test
    fun noSelectedByDefault() {
        SelectableMapViewModel(items).apply {
            selected.shouldBeNull()
        }
    }

    @Test
    fun selectedByDefault() {
        SelectableMapViewModel(items, 0).apply {
            selected shouldEqual 0
        }
    }

    @Test
    fun computeSelected() {
        SelectableMapViewModel(items, 0).apply {
            onSelectedComputed += { selected = it }
            computeSelected(1)
            selected shouldEqual 1
        }
    }
}
package selectable.map

import com.algolia.instantsearch.core.selectable.map.SelectableMapViewModel
import shouldBeNull
import shouldEqual
import kotlin.test.Test


class TestSelectableMapViewModel {

    private val items = mapOf(0 to "A", 1 to "B")

    @Test
    fun noSelectedByDefault() {
        SelectableMapViewModel(items).apply {
            selected.value.shouldBeNull()
        }
    }

    @Test
    fun computeSelected() {
        SelectableMapViewModel(items).apply {
            eventSelection.subscribe { selected.value = it }
            eventSelection.send(1)
            selected.value shouldEqual 1
        }
    }
}
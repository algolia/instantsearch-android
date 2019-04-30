package selection

import kotlin.test.Test
import kotlin.test.assertEquals

class TestSelectableViewModel {

    private val valueA = "valueA"
    private val valueB = "valueB"

    @Test
    fun selection() {
        SelectableViewModel<String>().apply {
            item = valueA
            onSelectionComputed += { selection = it }
            assertEquals(false, selection)
            toggleSelection()
            assertEquals(true, selection)
            toggleSelection()
            assertEquals(false, selection)
        }
    }
}

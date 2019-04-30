package selection

import kotlin.test.Test
import kotlin.test.assertEquals

class TestSelectableViewModel {

    private val valueA = "valueA"

    @Test
    fun selection() {
        SelectableViewModel<String>().apply {
            item = valueA
            onSelectionComputed += { selected = it }
            assertEquals(false, selected)

            toggleSelection()
            assertEquals(true, selected)

            toggleSelection()
            assertEquals(false, selected)
        }
    }
}

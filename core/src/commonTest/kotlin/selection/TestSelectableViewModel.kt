package selection

import kotlin.test.Test
import kotlin.test.assertEquals

class TestSelectableViewModel {

    private val valueA = "valueA"

    @Test
    fun selection() {
        SelectableViewModel(valueA).apply {
            onSelectedComputed += { selected = it }
            assertEquals(false, selected)

            toggleSelection()
            assertEquals(true, selected)

            toggleSelection()
            assertEquals(false, selected)
        }
    }
}

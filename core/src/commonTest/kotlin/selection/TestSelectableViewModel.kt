package selection

import kotlin.test.Test
import kotlin.test.assertEquals

class TestSelectableViewModel {

    private val valueA = "valueA"

    @Test
    fun selection() {
        SelectableViewModel(valueA).apply {
            onSelectedComputed += { isSelected = it }
            assertEquals(false, isSelected)

            setSelected(true)
            assertEquals(true, isSelected)

            setSelected(false)
            assertEquals(false, isSelected)
        }
    }
}

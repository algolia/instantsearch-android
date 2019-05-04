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

            setIsSelected(true)
            assertEquals(true, isSelected)

            setIsSelected(false)
            assertEquals(false, isSelected)
        }
    }
}

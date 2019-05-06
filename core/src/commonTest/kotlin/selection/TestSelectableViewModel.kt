package selection

import kotlin.test.Test
import kotlin.test.assertEquals

class TestSelectableViewModel {

    private val valueA = "valueA"

    @Test
    fun selection() {
        SelectableViewModel(valueA).apply {
            onIsSelectedComputed += { isSelected = it }
            assertEquals(false, isSelected)

            computeIsSelected(true)
            assertEquals(true, isSelected)

            computeIsSelected(false)
            assertEquals(false, isSelected)
        }
    }
}

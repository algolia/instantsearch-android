package selectable

import com.algolia.instantsearch.core.selectable.SelectableItemViewModel
import kotlin.test.Test
import kotlin.test.assertEquals


class TestSelectableItemViewModel {

    private val valueA = "valueA"

    @Test
    fun selection() {
        SelectableItemViewModel(valueA).apply {
            onIsSelectedComputed += { isSelected = it }
            assertEquals(false, isSelected)

            computeIsSelected(true)
            assertEquals(true, isSelected)

            computeIsSelected(false)
            assertEquals(false, isSelected)
        }
    }
}

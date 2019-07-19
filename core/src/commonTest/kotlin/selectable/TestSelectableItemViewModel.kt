package selectable

import com.algolia.instantsearch.core.selectable.SelectableItemViewModel
import shouldBeFalse
import shouldBeTrue
import kotlin.test.Test


class TestSelectableItemViewModel {

    private val valueA = "valueA"

    @Test
    fun selection() {
        SelectableItemViewModel(valueA).apply {
            eventSelection.subscribe { isSelected.value = it }
            isSelected.value.shouldBeFalse()
            eventSelection.send(true)
            isSelected.value.shouldBeTrue()
            eventSelection.send(false)
            isSelected.value.shouldBeFalse()
        }
    }
}

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
            eventSelection.subscribe { isSelected.set(it) }
            isSelected.get().shouldBeFalse()
            eventSelection.send(true)
            isSelected.get().shouldBeTrue()
            eventSelection.send(false)
            isSelected.get().shouldBeFalse()
        }
    }
}

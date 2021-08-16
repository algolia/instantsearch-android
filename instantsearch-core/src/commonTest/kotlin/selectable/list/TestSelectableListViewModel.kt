package selectable.list

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import shouldEqual
import kotlin.test.Test

class TestSelectableListViewModel {

    private val valueA = "valueA"
    private val valueB = "valueB"

    @Test
    fun singleChoice() {
        SelectableListViewModel<String, String>(selectionMode = SelectionMode.Single).apply {
            items.value = listOf(valueA, valueB)
            eventSelection.subscribe { selections.value = it }
            select(valueA)
            selections.value shouldEqual setOf(valueA)
            select(valueB)
            selections.value shouldEqual setOf(valueB)
            select(valueB)
            selections.value shouldEqual setOf()
        }
    }

    @Test
    fun multipleChoice() {
        SelectableListViewModel<String, String>(selectionMode = SelectionMode.Multiple).apply {
            items.value = listOf(valueA, valueB)
            eventSelection.subscribe { selections.value = it }
            select(valueA)
            selections.value shouldEqual setOf(valueA)
            select(valueB)
            selections.value shouldEqual setOf(valueA, valueB)
            select(valueB)
            selections.value shouldEqual setOf(valueA)
        }
    }
}

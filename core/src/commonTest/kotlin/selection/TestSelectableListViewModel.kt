package selection

import shouldEqual
import kotlin.test.Test


class TestSelectableListViewModel {

    private val valueA = "valueA"
    private val valueB = "valueB"

    @Test
    fun singleChoice() {
        SelectableListViewModel<String, String>(SelectionMode.Single).apply {
            items = listOf(valueA, valueB)
            onSelectionsComputed += { selections = it }
            selectItem(valueA)
            selections shouldEqual setOf(valueA)
            selectItem(valueB)
            selections shouldEqual setOf(valueB)
            selectItem(valueB)
            selections shouldEqual setOf()
        }
    }

    @Test
    fun multipleChoice() {
        SelectableListViewModel<String, String>(SelectionMode.Multiple).apply {
            items = listOf(valueA, valueB)
            onSelectionsComputed += { selections = it }
            selectItem(valueA)
            selections shouldEqual setOf(valueA)
            selectItem(valueB)
            selections shouldEqual setOf(valueA, valueB)
            selectItem(valueB)
            selections shouldEqual setOf(valueA)
        }
    }
}
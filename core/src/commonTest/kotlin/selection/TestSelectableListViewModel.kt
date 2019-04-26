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
            select(valueA)
            selections shouldEqual setOf(valueA)
            select(valueB)
            selections shouldEqual setOf(valueB)
            select(valueB)
            selections shouldEqual setOf()
        }
    }

    @Test
    fun multipleChoice() {
        SelectableListViewModel<String, String>(SelectionMode.Multiple).apply {
            items = listOf(valueA, valueB)
            onSelectionsComputed += { selections = it }
            select(valueA)
            selections shouldEqual setOf(valueA)
            select(valueB)
            selections shouldEqual setOf(valueA, valueB)
            select(valueB)
            selections shouldEqual setOf(valueA)
        }
    }
}
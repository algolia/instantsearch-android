package selection

import shouldEqual
import kotlin.test.Test


class TestSelectionListViewModel {

    private val valueA = "valueA"
    private val valueB = "valueB"

    @Test
    fun singleChoice() {
        SelectionListViewModel<String, String>(SelectionMode.Single).apply {
            values = listOf(valueA, valueB)
            onSelectedChange += { selections = it }
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
        SelectionListViewModel<String, String>(SelectionMode.Multiple).apply {
            values = listOf(valueA, valueB)
            onSelectedChange += { selections = it }
            select(valueA)
            selections shouldEqual setOf(valueA)
            select(valueB)
            selections shouldEqual setOf(valueA, valueB)
            select(valueB)
            selections shouldEqual setOf(valueA)
        }
    }
}
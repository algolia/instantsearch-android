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
            selections shouldEqual listOf(valueA)
            select(valueB)
            selections shouldEqual listOf(valueB)
            select(valueB)
            selections shouldEqual listOf()
        }
    }

    @Test
    fun multipleChoice() {
        SelectionListViewModel<String, String>(SelectionMode.Multiple).apply {
            values = listOf(valueA, valueB)
            onSelectedChange += { selections = it }
            select(valueA)
            selections shouldEqual listOf(valueA)
            select(valueB)
            selections shouldEqual listOf(valueA, valueB)
            select(valueB)
            selections shouldEqual listOf(valueA)
        }
    }
}
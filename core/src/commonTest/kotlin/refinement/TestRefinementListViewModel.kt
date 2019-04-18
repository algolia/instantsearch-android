package refinement

import shouldEqual
import kotlin.test.Test


class TestRefinementListViewModel {

    private val refinementA = "valueA"
    private val refinementB = "valueB"
    private val refinementC = "valueC"

    class MockRefinementListViewModel(
        selectionMode: SelectionMode
    ) : RefinementListViewModel<String, String>(selectionMode) {

        override fun String.match(key: String): Boolean {
            return this == key
        }
    }

    @Test
    fun singleChoice() {
        MockRefinementListViewModel(SelectionMode.SingleChoice).apply {
            refinements = listOf(
                refinementA,
                refinementB
            )
            selectedListeners += { selections = it }
            select(refinementA)
            selections shouldEqual listOf(refinementA)
            select(refinementB)
            selections shouldEqual listOf(refinementB)
            select(refinementB)
            selections shouldEqual listOf()
        }
    }

    @Test
    fun multipleChoice() {
        MockRefinementListViewModel(SelectionMode.MultipleChoice).apply {
            refinements = listOf(
                refinementA,
                refinementB
            )
            selectedListeners += { selections = it }
            select(refinementA)
            selections shouldEqual listOf(refinementA)
            select(refinementB)
            selections shouldEqual listOf(refinementA, refinementB)
            select(refinementB)
            selections shouldEqual listOf(refinementA)
        }
    }

    @Test
    fun persistentSelectionOff() {
        MockRefinementListViewModel(SelectionMode.SingleChoice).apply {
            persistentSelection = false
            refinements = listOf(
                refinementA,
                refinementB,
                refinementC
            )
            selectedListeners += { selections = it }
            select(refinementC)
            selections shouldEqual listOf(refinementC)
            refinements = listOf(
                refinementA,
                refinementB
            )
            selections shouldEqual listOf()
        }
    }

    @Test
    fun persistentSelectionOn() {
        MockRefinementListViewModel(SelectionMode.SingleChoice).apply {
            selectedListeners += { selections = it }
            persistentSelection = true
            refinements = listOf(
                refinementA,
                refinementB,
                refinementC
            )
            select(refinementC)
            selections shouldEqual listOf(refinementC)
            refinements = listOf(
                refinementA,
                refinementB
            )
            selections shouldEqual listOf(refinementC)
        }
    }
}
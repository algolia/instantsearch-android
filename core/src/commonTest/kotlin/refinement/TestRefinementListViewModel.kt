package refinement

import shouldEqual
import kotlin.test.Test


class TestRefinementListViewModel {

    private val refinementA = "valueA"
    private val refinementB = "valueB"
    private val refinementC = "valueC"

    @Test
    fun singleChoice() {
        RefinementListViewModel<String>(SelectionMode.SingleChoice).apply {
            refinements = listOf(
                refinementA,
                refinementB
            )
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
        RefinementListViewModel<String>(SelectionMode.MultipleChoice).apply {
            refinements = listOf(
                refinementA,
                refinementB
            )
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
        RefinementListViewModel<String>().apply {
            persistentSelection = false
            refinements = listOf(
                refinementA,
                refinementB,
                refinementC
            )
            select(refinementC)
            refinements = listOf(
                refinementA,
                refinementB
            )
            selections shouldEqual listOf()
        }
    }

    @Test
    fun persistentSelectionOn() {
        RefinementListViewModel<String>().apply {
            persistentSelection = true
            refinements = listOf(
                refinementA,
                refinementB,
                refinementC
            )
            select(refinementC)
            refinements = listOf(
                refinementA,
                refinementB
            )
            selections shouldEqual listOf(refinementC)
        }
    }
}
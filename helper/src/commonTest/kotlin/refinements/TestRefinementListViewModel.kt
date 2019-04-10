package refinements

import refinement.RefinementListViewModel
import shouldEqual
import kotlin.test.Test


class TestRefinementListViewModel {

    private val refinementA = "valueA"
    private val refinementB = "valueB"
    private val refinementC = "valueC"

    @Test
    fun singleChoice() {
        RefinementListViewModel<String>(RefinementListViewModel.Mode.SingleChoice).apply {
            refinements = listOf(
                refinementA,
                refinementB
            )
            select(refinementA)
            selected shouldEqual listOf(refinementA)
            select(refinementB)
            selected shouldEqual listOf(refinementB)
            select(refinementB)
            selected shouldEqual listOf()
        }
    }

    @Test
    fun multipleChoice() {
        RefinementListViewModel<String>(RefinementListViewModel.Mode.MultipleChoice).apply {
            refinements = listOf(
                refinementA,
                refinementB
            )
            select(refinementA)
            selected shouldEqual listOf(refinementA)
            select(refinementB)
            selected shouldEqual listOf(refinementA, refinementB)
            select(refinementB)
            selected shouldEqual listOf(refinementA)
        }
    }

    @Test
    fun persistentSelectionOff() {
        RefinementListViewModel<String>().apply {
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
            selected shouldEqual listOf()
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
            selected shouldEqual listOf(refinementC)
        }
    }
}
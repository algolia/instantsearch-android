package refinement

import shouldEqual
import kotlin.test.Test


class TestRefinementViewModel {

    private val valueA = "valueA"
    private val valueB = "valueB"

    @Test
    fun selection() {
        RefinementViewModel<String>().apply {
            refinement = valueA
            select(valueA)
            selected shouldEqual valueA
            select(null)
            selected shouldEqual null
        }
    }

    @Test
    fun persistentSelectionOff() {
        RefinementViewModel<String>().apply {
            persistentSelection = false
            refinement = valueA
            select(valueA)
            refinement = valueB
            selected shouldEqual null
        }
    }

    @Test
    fun persistentSelectionOn() {
        RefinementViewModel<String>().apply {
            persistentSelection = true
            refinement = valueA
            select(valueA)
            refinement = valueB
            selected shouldEqual valueA
        }
    }
}
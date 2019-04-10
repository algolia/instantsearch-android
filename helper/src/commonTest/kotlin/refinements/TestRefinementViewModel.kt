package refinements

import refinement.RefinementViewModel
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
}
package refinement

import shouldEqual
import kotlin.test.Test


class TestRefinementPresenter {

    private class MockRefinementPresenter : RefinementPresenter<String>() {

        override val comparator = Comparator<RefinedData<String>> { a, b ->
            b.first.compareTo(a.first)
        }
    }

    private val refinements = listOf(
        "a" to false,
        "b" to true,
        "c" to false,
        "d" to true,
        "e" to false
    )

    @Test
    fun comparator() {
        MockRefinementPresenter().apply {
            data = refinements
            data shouldEqual data.sortedByDescending { it.first }
        }
    }

    @Test
    fun limit() {
        MockRefinementPresenter().apply {
            data = refinements
            limit = 1
            data shouldEqual listOf(refinements.last())
        }
    }
}
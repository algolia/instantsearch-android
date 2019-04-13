package refinement

import shouldEqual
import kotlin.test.Test


class TestRefinementPresenter {

    private class MockRefinementListPresenter : RefinementListPresenter<String>() {

        override val comparator = Comparator<SelectedRefinement<String>> { a, b ->
            b.first.compareTo(a.first)
        }
    }

    private val list = listOf(
        "a" to false,
        "b" to true,
        "c" to false,
        "d" to true,
        "e" to false
    )

    @Test
    fun comparator() {
        MockRefinementListPresenter().apply {
            refinements = list
            refinements shouldEqual list.sortedByDescending { it.first }
        }
    }

    @Test
    fun limit() {
        MockRefinementListPresenter().apply {
            refinements = list
            limit = 1
            refinements shouldEqual listOf(list.last())
        }
    }
}
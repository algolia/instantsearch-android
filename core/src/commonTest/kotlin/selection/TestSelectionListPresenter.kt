package selection

import shouldEqual
import kotlin.test.Test


class TestSelectionListPresenter {

    private class MockSelectionListPresenter : SelectionListPresenter<String>() {

        override val comparator = Comparator<SelectableItem<String>> { a, b ->
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
        MockSelectionListPresenter().apply {
            values = list
            values shouldEqual list.sortedByDescending { it.first }
        }
    }

    @Test
    fun limit() {
        MockSelectionListPresenter().apply {
            values = list
            limit = 1
            values shouldEqual listOf(list.last())
        }
    }
}
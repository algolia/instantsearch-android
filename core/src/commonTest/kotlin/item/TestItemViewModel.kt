package item

import com.algolia.instantsearch.core.item.ItemViewModel
import shouldBeNull
import shouldBeTrue
import kotlin.test.Test


class TestItemViewModel {

    @Test
    fun itemShouldBeNullByDefault() {
        val viewModel = ItemViewModel<Unit>()

        viewModel.item.shouldBeNull()
    }

    @Test
    fun setItemShouldCallOnItemChanged() {
        val viewModel = ItemViewModel<Unit>()
        var changed = false

        viewModel.onItemChanged += { changed = true }
        viewModel.item = Unit
        changed.shouldBeTrue()
    }
}
package map

import com.algolia.instantsearch.core.map.MapViewModel
import shouldEqual
import kotlin.test.Test


class TestMapViewModel {

    private val id = "id"
    private val map = mapOf(id to "value")

    @Test
    fun removeShouldCallOnMapComputed() {
        val viewModel = MapViewModel(map)

        viewModel.event.subscribe { viewModel.map.set(it.get()) }
        viewModel.remove(id)
        viewModel.map.get() shouldEqual mapOf()
    }
}
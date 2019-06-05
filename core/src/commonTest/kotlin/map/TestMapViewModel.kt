package map

import com.algolia.instantsearch.core.map.MapViewModel
import shouldBeTrue
import kotlin.test.Test


class TestMapViewModel {

    private val green = "green"
    private val red = "red"

    @Test
    fun removeShouldCallOnMapComputed() {
        val viewModel = MapViewModel(mapOf(green to green))
        var triggered = false

        viewModel.onMapComputed += {
            triggered = true
        }
        viewModel.remove(green)
        triggered.shouldBeTrue()
    }
}
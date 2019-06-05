package map

import com.algolia.instantsearch.core.map.MapViewModel
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestMapViewModel {

    private val green = "green"
    private val red = "red"

    @Test
    fun removeTriggersOnMapComputed() {
        val viewModel = MapViewModel(mapOf(green to green))
        var triggered = false

        viewModel.onMapComputed += {
            triggered = true
        }
        viewModel.remove(green)
        triggered.shouldBeTrue()
    }

    @Test
    fun removeHitsThenKeep() {
        val viewModel = MapViewModel(mapOf(green to green))

        viewModel.onMapComputed += {
            it shouldEqual mapOf(green to green)
        }
        viewModel.remove(red)
    }


    @Test
    fun removeHitsThenRemove() {
        val viewModel = MapViewModel(mapOf(green to green))

        viewModel.onMapComputed += {
            it shouldEqual mapOf()
        }
        viewModel.remove(green)
    }
}
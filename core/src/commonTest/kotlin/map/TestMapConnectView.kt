package map

import com.algolia.instantsearch.core.map.MapView
import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.map.connectView
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestMapConnectView {

    private val id = 0
    private val map = mapOf(id to "value")

    private class MockMapView : MapView<Int, String> {

        var map: Map<Int, String>? = null

        override var onClick: ((Int) -> Unit)? = null

        override fun setItem(item: Map<Int, String>) {
            map = item
        }
    }

    @Test
    fun connectShouldSetItems() {
        val viewModel = MapViewModel(map)
        val view = MockMapView()

        viewModel.connectView(view)
        view.map shouldEqual map
    }

    @Test
    fun onClickShouldCallClear() {
        val viewModel = MapViewModel(map)
        val view = MockMapView()

        viewModel.onMapComputed += {
            viewModel.item = it
        }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(id)
        view.map shouldEqual mapOf()
    }

    @Test
    fun onItemChangedShouldCallSetItems() {
        val viewModel = MapViewModel(map)
        val view = MockMapView()

        viewModel.connectView(view)
        viewModel.map = mapOf()
        view.map shouldEqual mapOf()
    }
}
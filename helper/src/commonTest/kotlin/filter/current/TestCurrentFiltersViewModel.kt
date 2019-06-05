package filter.current

import com.algolia.instantsearch.helper.filter.current.CurrentFiltersViewModel
import com.algolia.instantsearch.helper.filter.current.filters
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestCurrentFiltersViewModel {

    private val green = "green"
    private val red = "red"
    private val color = Attribute("color")
    private val filterRed = Filter.Facet(color, red)
    private val filterGreen = Filter.Facet(color, green)
    private val filters = mapOf<String, Filter>(red to filterRed, green to filterGreen)

    @Test
    fun clearFilterMissesThenNoClear() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))

        viewModel.remove(red)
        viewModel.item shouldEqual mapOf(green to filterGreen)
    }


    @Test
    fun clearFilterTriggersListeners() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))
        var triggered = false

        viewModel.onMapComputed += {
            triggered = true
        }
        viewModel.remove(green)
        triggered.shouldBeTrue()
    }

    @Test
    fun getValuesReturnsFilterSet() {
        val viewModel = CurrentFiltersViewModel(mapOf(green to filterGreen))

        viewModel.filters shouldEqual setOf(filterGreen)
    }
}
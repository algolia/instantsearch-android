package filter.current

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.helper.android.filter.current.CurrentFiltersViewImpl
import com.algolia.instantsearch.helper.filter.current.CurrentFiltersViewModel
import com.algolia.instantsearch.helper.filter.current.connectFilterState
import com.algolia.instantsearch.helper.filter.current.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.google.android.material.chip.ChipGroup
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual
import shouldNotBeEmpty


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestCurrentFiltersViewImpl {

    private val color = Attribute("color")
    private val groupID = FilterGroupID(color)
    private val filterRed = Filter.Facet(color, "red")
    private val filterGreen = Filter.Facet(color, "green")
    private val filters = mapOf(groupID to setOf(filterRed, filterGreen))

    private fun chipGroup(): ChipGroup = ChipGroup(applicationContext)

    @Test
    fun onViewClickCallsClearFilters() {
        val filterState = FilterState(filters)
        val viewModel = CurrentFiltersViewModel()
        val view = CurrentFiltersViewImpl(chipGroup())

        viewModel.connectFilterState(filterState)
        viewModel.connectView(view)

        filterState.getFilters().shouldNotBeEmpty()
        view.view.getChildAt(0).callOnClick()
        filterState.getFilters() shouldEqual setOf(filterGreen)
    }
}
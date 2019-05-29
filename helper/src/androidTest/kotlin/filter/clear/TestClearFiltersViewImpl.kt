package filter.clear

import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.helper.android.filter.clear.ClearFiltersViewImpl
import com.algolia.instantsearch.helper.filter.clear.ClearFiltersViewModel
import com.algolia.instantsearch.helper.filter.clear.connectFilterState
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldBeEmpty
import shouldNotBeEmpty


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class TestClearFiltersViewImpl {

    private val color = Attribute("color")
    private val groupID = FilterGroupID.And(color)
    private val filterRed = Filter.Facet(color, "red")

    private fun view() : View = View(ApplicationProvider.getApplicationContext())


    @Test
    fun onViewClickCallsClearFilters() {
        val filterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(filterRed)))
        val viewModel = ClearFiltersViewModel()
        val view = ClearFiltersViewImpl(view())

        viewModel.connectFilterState(filterState)
        viewModel.connectView(view)
        filterState.getFilters().shouldNotBeEmpty()
        view.view.callOnClick()
        filterState.getFilters().shouldBeEmpty()
    }
}
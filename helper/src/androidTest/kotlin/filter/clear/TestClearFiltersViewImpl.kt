package filter.clear

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
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
import shouldEqual

@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class TestClearFiltersViewImpl {
    private fun view() : View = View(applicationContext)

    @Test
    fun onViewClickCallsClearFilters() {
        val filterState = FilterState().apply {
            add(FilterGroupID.And("foo"), Filter.Facet(Attribute("color"), "red"))
        }
        val viewModel = ClearFiltersViewModel()
        val view = ClearFiltersViewImpl(view())
        viewModel.connectFilterState(filterState)
        viewModel.connectView(view)

        view.view.callOnClick()
        filterState.getFilters() shouldEqual setOf()
    }
}